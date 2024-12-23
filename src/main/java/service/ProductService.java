package service;

import dto.ProductRequest;
import dto.ProductResponse;
import entity.Category;
import entity.Product;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import repository.ProductRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ConversionService conversionService;
    private final ProductRepository productRepository;

    private boolean checkCategory(String checkingField) {
        try {
            Enum.valueOf(Category.class, checkingField);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public ProductResponse createProduct(ProductRequest productDto) {
        if (!productDto.getName().isEmpty()
                && checkCategory(productDto.getCategories().toString())
                && !productDto.getPrice().isNaN()) {
            Product product = conversionService.convert(productDto, Product.class);
            productRepository.save(product);
            log.info("Created product {}", productDto);
            return conversionService.convert(product, ProductResponse.class);
        } else {
            log.error("Invalid product request {}", productDto);
            return null;
        }
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> conversionService.convert(product, ProductResponse.class))
                .collect(Collectors.toList());
    }

    public void deleteProduct(String productArticle) {
        try {
            productRepository.deleteById(UUID.fromString(productArticle));
            log.info("Deleted product {}", productArticle);
        } catch (Exception e) {
            log.error("Error while deleting product {}", productArticle, e);
        }
    }

    public ProductResponse update(ProductRequest productDto, String productArticle) {
        Product entity = null;
        try {
            entity = productRepository.findByArticle(productArticle);
        } catch (Exception e) {
            log.error("Error while updating product {}", productArticle, e);
            return null;
        }
        if (!productDto.getName().isEmpty()) entity.setName(productDto.getName());
        if (!productDto.getDescription().isEmpty()) entity.setDescription(productDto.getDescription());
        if (!productDto.getPrice().isNaN()) entity.setPrice(productDto.getPrice());
        if (productDto.getCategories() != null) entity.setCategories(productDto.getCategories());
        if (productDto.getQuantity() != null) entity.setQuantity(productDto.getQuantity());
        entity.setDateOfLastChangesQuantity(new Timestamp(System.currentTimeMillis()));
        productRepository.save(entity);
        log.info("Updated product {}", entity);
        return conversionService.convert(entity, ProductResponse.class);
    }

}
