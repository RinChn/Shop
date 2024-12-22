package app.service;

import app.dto.ProductRequest;
import app.entities.Category;
import app.entities.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DtoProductToEntityConverter implements Converter<ProductRequest, Product> {
    @Override
    public Product convert(ProductRequest source) {
        return Product.builder()
                .name(source.getName())
                .description(source.getDescription())
                .quantity(source.getQuantity())
                .price(source.getPrice())
                .categories(Category.valueOf(source.getCategories()))
                .dateOfLastChangesQuantity(source.getDateOfLastChangesQuantity())
                .dateOfCreation(source.getDateOfCreation())
                .build();

    }
}
