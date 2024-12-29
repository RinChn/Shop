package marketplace.service;

import marketplace.dto.ProductRequestUpdate;
import marketplace.dto.ProductRequestCreate;
import marketplace.dto.ProductResponse;
import marketplace.entity.Product;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
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
        Product resultCheckingProduct = productRepository.findByNameAndDescriptionAndCategories(product.getName(),
                product.getDescription(),
                product.getCategories());
        if (resultCheckingProduct == null) {
            productRepository.save(product);
            log.info("Created product {}", productDto);
            return conversionService.convert(product, ProductResponse.class);
        } else {
            log.info("Product {} already exists", productDto);
            return conversionService.convert(resultCheckingProduct, ProductResponse.class);
        }

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
        Product resultSearch = productRepository.findByArticle(productArticle);
        return conversionService.convert(resultSearch, ProductResponse.class);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer productArticle) {
        try {
            productRepository.delete(productRepository.findByArticle(productArticle));
            log.info("Deleted product {}", productArticle);
        } catch (Exception e) {
            log.error("Error while deleting product {}", productArticle, e);
        }
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(ProductRequestUpdate productDto, Integer productArticle) {
        Product entity;
        try {
            entity = productRepository.findByArticle(productArticle);
        } catch (Exception e) {
            log.error("Error while updating product {}", productArticle, e);
            return null;
        }
        Optional.ofNullable(productDto.getName())
                .ifPresent(entity::setName);
        Optional.ofNullable(productDto.getDescription())
                .ifPresent(entity::setDescription);
        Optional.ofNullable(productDto.getPrice())
                .ifPresent(entity::setPrice);
        Optional.ofNullable(productDto.getCategories())
                .ifPresent(entity::setCategories);
        Optional.ofNullable(productDto.getQuantity())
                .ifPresent(entity::setQuantity);

        entity.setDateOfLastChangesQuantity(new Timestamp(System.currentTimeMillis()));
        productRepository.save(entity);
        log.info("Updated product {}", entity);
        return conversionService.convert(entity, ProductResponse.class);
    }
}
