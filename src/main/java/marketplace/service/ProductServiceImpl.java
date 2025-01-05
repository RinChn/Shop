package marketplace.service;

import marketplace.dto.ProductRequestUpdate;
import marketplace.dto.ProductRequestCreate;
import marketplace.dto.ProductResponse;
import marketplace.entity.Product;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import marketplace.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        log.info("Created product {}", productDto);
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
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_ARTICLE));;
        productRepository.delete(product);
        log.info("Deleted product {}", productArticle);
        return product.getId();
    }

    @Override
    @Transactional
    @Timer
    public void deleteAllProducts() {
        productRepository.deleteAll();
        log.info("Deleted all products");
    }

    @Override
    @Transactional
    @Timer
    public ProductResponse updateProduct(ProductRequestUpdate request, Integer productArticle) {
        Product entity;
        entity = productRepository
                .findByArticle(productArticle)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_ARTICLE));
        Optional.ofNullable(request.getName())
                .ifPresent(entity::setName);
        Optional.ofNullable(request.getDescription())
                .ifPresent(entity::setDescription);
        Optional.ofNullable(request.getPrice())
                .ifPresent(entity::setPrice);
        Optional.ofNullable(request.getCategories())
                .ifPresent(entity::setCategories);
        Optional.ofNullable(request.getQuantity())
                .ifPresent(entity::setQuantity);

        entity.setDateOfLastChangesQuantity(new Timestamp(System.currentTimeMillis()));
        productRepository.save(entity);
        log.info("Updated product {}", entity);
        return conversionService.convert(entity, ProductResponse.class);
    }


}
