package marketplace.service;

import marketplace.aspect.Timer;
import marketplace.controller.response.OrderResponse;
import marketplace.dto.SearchFilter;
import marketplace.controller.request.ProductRequestUpdate;
import marketplace.controller.request.ProductRequestCreate;
import marketplace.controller.response.ProductResponse;
import marketplace.entity.OrderComposition;
import marketplace.entity.Product;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import marketplace.repository.OrderCompositionRepository;
import marketplace.util.FileHandler;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import marketplace.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ConversionService conversionService;
    private final ProductRepository productRepository;
    private final OrderCompositionRepository orderCompositionRepository;

    @Override
    @Transactional
    @Timer
    public ProductResponse createProduct(ProductRequestCreate productDto) {
        Product product = conversionService.convert(productDto, Product.class);
        productRepository.findByArticle(product.getArticle())
                .ifPresent(resultCheckingProduct -> {
                    throw new ApplicationException(ErrorType.DUPLICATE);
                });
        productRepository.save(product);
        log.info("Created product with article {}", product.getArticle());
        return conversionService.convert(product, ProductResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    @Timer
    public List<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize) {
        return productRepository.findAll(PageRequest.of(pageNumber, pageSize))
                .stream()
                .map(product -> conversionService.convert(product, ProductResponse.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Timer
    public ProductResponse getProduct(Integer productArticle) {
        Product resultSearch = productRepository
                .findByArticle(productArticle)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_ARTICLE));
        return conversionService.convert(resultSearch, ProductResponse.class);
    }

    @Override
    @Transactional
    @Timer
    public UUID deleteProduct(Integer productArticle) {
        Product product = productRepository
                .findByArticle(productArticle)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_ARTICLE));
        productRepository.delete(product);
        log.info("Deleted product with article {}", productArticle);
        return product.getId();
    }

    @Override
    @Transactional
    @Timer
    public ProductResponse updateProduct(ProductRequestUpdate request, Integer productArticle) {
        Product product = productRepository
                .findByArticle(productArticle)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_ARTICLE));
        setAllFieldsIfNotNull(product, request);
        productRepository.save(product);
        log.info("Updated product with article {}", product.getArticle());
        return conversionService.convert(product, ProductResponse.class);
    }

    private void setAllFieldsIfNotNull(Product product, ProductRequestUpdate request) {
        setFieldIfNotNull(request.getName(), product::setName);
        setFieldIfNotNull(request.getDescription(), product::setDescription);
        setFieldIfNotNull(request.getPrice(), product::setPrice);
        setFieldIfNotNull(request.getCategories(), product::setCategories);
        setFieldIfNotNull(request.getQuantity(), product::setQuantity);
        setFieldIfNotNull(request.getIsAvailable(), product::setIsAvailable);
    }

    private <T> void setFieldIfNotNull(T value, Consumer<T> setter) {
        Optional.ofNullable(value).ifPresent(setter);
    }

    @Override
    @Timer
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(SearchFilter searchFilter) {
        List<Product> foundProducts = productRepository.searchUsingFilter(searchFilter.getName(), searchFilter.getQuantity(),
                searchFilter.getPrice(), searchFilter.getIsAvailable());
        FileHandler.AddingListOfProductsToExcel(foundProducts);
        return foundProducts.stream()
                .map(product -> conversionService.convert(product, ProductResponse.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Timer
    @Override
    public Map<Integer, List<OrderResponse>> getAllOrderForEveryProduct() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .collect(Collectors.toMap(
                        Product::getArticle,
                        product -> orderCompositionRepository.findCompositionsOfProduct(product)
                                .stream().map(OrderComposition::getOrder)
                                .map(order -> conversionService.convert(order, OrderResponse.class))
                                .toList()
                ));
    }

    @Transactional(readOnly = true)
    @Timer
    @Override
    public List<OrderResponse> getOrdersForProduct(Integer productArticle) {
        Product product = productRepository
                .findByArticle(productArticle)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_ARTICLE));
        return orderCompositionRepository.findCompositionsOfProduct(product)
                .stream()
                .map(OrderComposition::getOrder)
                .map(order -> conversionService.convert(order, OrderResponse.class))
                .toList();
    }

    @Transactional
    @Timer
    @Override
    public Product bookProduct(Integer productArticle, Integer quantity) {
        Product product = productRepository.findByArticle(productArticle)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_ARTICLE));
        if (productRepository.bookProduct(product.getId(), quantity) == 0)
            throw new ApplicationException(ErrorType.NOT_ENOUGH_PRODUCTS);
        productRepository.save(product);
        log.info("Booked {} products with article {}", quantity, product.getArticle());
        return product;
    }

    @Transactional
    @Timer
    @Override
    public void returnOfProductsToWarehouse(Map<Product, Integer> products) {
        Product product;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            product = entry.getKey();
            product.setQuantity(product.getQuantity() + entry.getValue());
        }
        productRepository.saveAll(new ArrayList<>(products.keySet()));
    }

}
