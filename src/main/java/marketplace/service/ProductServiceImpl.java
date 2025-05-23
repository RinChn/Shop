package marketplace.service;

import feign.FeignException;
import marketplace.aspect.Timer;
import marketplace.controller.response.OrderAndTinResponse;
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
import marketplace.exchange.ExchangeTaxHandler;
import marketplace.exchange.TaxFeignClient;
import marketplace.repository.OrderCompositionRepository;
import marketplace.util.FileHandler;
import marketplace.util.UserHandler;
import org.springframework.cache.annotation.Cacheable;
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
    private final ExchangeTaxHandler exchangeTaxHandler;

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
    public Map<Integer, List<OrderAndTinResponse>> getAllOrderForEveryProduct() {
        List<Product> products = productRepository.findAll();
        Map<Integer, List<OrderAndTinResponse>> allOrders = products.stream()
                .collect(Collectors.toMap(
                        Product::getArticle,
                        product -> orderCompositionRepository.findCompositionsOfProduct(product)
                                .stream().map(OrderComposition::getOrder)
                                .map(order -> conversionService.convert(order, OrderAndTinResponse.class))
                                .toList()
                ));
        Set<String> uniqueEmails = allOrders.values().stream()
                .flatMap(List::stream)
                .map(OrderAndTinResponse::getEmailCustomer)
                .collect(Collectors.toSet());

        List<String> tins = exchangeTaxHandler.getTins(uniqueEmails.stream().toList());

        if (!tins.isEmpty()) {
            Map<String, String> emailToTinMap = new HashMap<>();
            List<String> uniqueEmailList = new ArrayList<>(uniqueEmails);
            for (int i = 0; i < uniqueEmailList.size(); i++) {
                emailToTinMap.put(uniqueEmailList.get(i), tins.get(i));
            }
            allOrders.values().forEach(orderList ->
                    orderList.forEach(order -> {
                        String email = order.getEmailCustomer();
                        order.setTinCustomer(emailToTinMap.get(email));
                    })
            );
        } else {
            allOrders.values().forEach(orderList ->
                    orderList.forEach(order -> {
                        order.setTinCustomer("The tax service is unavailable");
                    })
            );
        }
        return allOrders;

    }

    @Transactional(readOnly = true)
    @Timer
    @Override
    public List<OrderAndTinResponse> getOrdersForProduct(Integer productArticle) {
        Product product = productRepository
                .findByArticle(productArticle)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_ARTICLE));
        List<OrderAndTinResponse> allOrders = orderCompositionRepository.findCompositionsOfProduct(product)
                .stream()
                .map(OrderComposition::getOrder)
                .map(order -> conversionService.convert(order, OrderAndTinResponse.class))
                .toList();
        Set<String> uniqueEmails = allOrders.stream()
                .map(OrderAndTinResponse::getEmailCustomer)
                .collect(Collectors.toSet());
        List<String> tins = exchangeTaxHandler.getTins(uniqueEmails.stream().toList());
        if (!tins.isEmpty()) {
            Map<String, String> emailToTinMap = new HashMap<>();
            List<String> uniqueEmailList = new ArrayList<>(uniqueEmails);
            for (int i = 0; i < uniqueEmailList.size(); i++) {
                emailToTinMap.put(uniqueEmailList.get(i), tins.get(i));
            }
            allOrders.forEach(order -> {
                        String email = order.getEmailCustomer();
                        order.setTinCustomer(emailToTinMap.get(email));
                    }
            );
        } else {
            allOrders.forEach(order -> {
                        order.setTinCustomer("The tax service is unavailable");
                    }
            );
        }
        log.info("Successfully get orders for product: {}", productArticle);
        return allOrders;
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
            log.info("Returned product {} with quantity {}", product, product.getQuantity());
        }
        productRepository.saveAll(new ArrayList<>(products.keySet()));
    }

}
