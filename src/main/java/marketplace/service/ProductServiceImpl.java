package marketplace.service;

import marketplace.dto.ProductRequestUpdate;
import marketplace.dto.ProductRequestCreate;
import marketplace.dto.ProductResponse;
import marketplace.entity.Product;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import marketplace.exceptions.DuplicateEntityException;
import marketplace.exceptions.NonexistentProductArticleException;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import marketplace.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ConversionService conversionService;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequestCreate productDto) {
        Product product = conversionService.convert(productDto, Product.class);
        productRepository.findByArticle(product.getArticle())
                .ifPresent(resultCheckingProduct -> {
                    throw new DuplicateEntityException(product.getArticle());
                });
        productRepository.save(product);
        log.info("Created product {}", productDto);
        return conversionService.convert(product, ProductResponse.class);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> conversionService.convert(product, ProductResponse.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Integer productArticle) {
        Product resultSearch = productRepository
                .findByArticle(productArticle)
                .orElseThrow(() -> new NonexistentProductArticleException(productArticle));
        return conversionService.convert(resultSearch, ProductResponse.class);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer productArticle) {
        Product product = productRepository
                .findByArticle(productArticle)
                .orElseThrow(() -> new NonexistentProductArticleException(productArticle));;
        productRepository.delete(product);
        log.info("Deleted product {}", productArticle);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(ProductRequestUpdate request, Integer productArticle) {
        Product entity;
        entity = productRepository
                .findByArticle(productArticle)
                .orElseThrow(() -> new NonexistentProductArticleException(productArticle));
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
