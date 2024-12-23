package service;

import dto.ProductResponse;
import entities.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductToDtoConverter implements Converter<Product, ProductResponse> {

    @Override
    public ProductResponse convert(Product source) {
        return  ProductResponse.builder()
                .name(source.getName())
                .description(source.getDescription())
                .price(source.getPrice())
                .categories(source.getCategories())
                .quantity(source.getQuantity())
                .dateOfLastChangesQuantity(source.getDateOfLastChangesQuantity())
                .dateOfCreation(source.getDateOfCreation())
                .articleNumber(source.getArticleNumber())
                .build();
    }
}
