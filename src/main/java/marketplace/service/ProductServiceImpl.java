package marketplace.service;

import marketplace.aspect.Timer;
import marketplace.dto.Filter;
import marketplace.dto.ProductRequestUpdate;
import marketplace.dto.ProductRequestCreate;
import marketplace.dto.ProductResponse;
import marketplace.entity.Product;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import marketplace.util.FileHandler;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import marketplace.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ConversionService conversionService;
    private final ProductRepository productRepository;

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
    public List<ProductResponse> searchProducts(Filter filter) {
        List<Product> foundProducts = productRepository.searchUsingFilter(filter.getName(), filter.getQuantity(),
                filter.getPrice(), filter.getIsAvailable());
        FileHandler.AddingListOfProductsToExcel(foundProducts);
        return foundProducts.stream()
                .map(product -> conversionService.convert(product, ProductResponse.class))
                .toList();
    }

    public Product bookProduct(Integer productArticle, Integer quantity) {
        Product product = productRepository.findByArticle(productArticle)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_ARTICLE));
        if (productRepository.bookProduct(product.getId(), quantity) == 0)
            throw new ApplicationException(ErrorType.NOT_ENOUGH_PRODUCTS);
        productRepository.save(product);
        log.info("Booked {} products with article {}", quantity, product.getArticle());
        return product;
    }

}
