package app.service;

import app.dto.ProductRequest;
import app.dto.ProductResponse;
import app.entities.Category;
import app.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import app.repository.ProductRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ConversionService conversionService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ConversionService conversionService, ProductRepository productRepository) {
        this.conversionService = conversionService;
        this.productRepository = productRepository;
    }

    private boolean checkCategory(String checkingField) {
        try {
            Enum.valueOf(Category.class, checkingField);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String createProduct(ProductRequest productDto) {
        if (!productDto.getName().isEmpty()
                && checkCategory(productDto.getCategories())
                && !productDto.getPrice().isNaN()) {
            productRepository.save(conversionService.convert(productDto, Product.class));
            return "Product created successfully";
        } else {
            return "Product creation failed";
        }
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> conversionService.convert(product, ProductResponse.class))
                .collect(Collectors.toList());
    }

    public String deleteProduct(String productUUID) {
        try {
            productRepository.deleteById(UUID.fromString(productUUID));
            return "Product deleted successfully";
        } catch (Exception e) {
            return "Product not found";
        }
    }

    public String updateName(String productUUID, String newName) {
        Product entity;
        try {
            entity = productRepository.findById(UUID.fromString(productUUID)).get();
        } catch (Exception e) {
            return "Product not found";
        }
        entity.setName(newName);
        entity.setDateOfLastChangesQuantity(new Timestamp(System.currentTimeMillis()));
        productRepository.save(entity);
        return "Product updated successfully.";
    }

}
