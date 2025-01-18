package app.marketplace.converter;

import app.marketplace.dto.ProductResponse;
import app.marketplace.entity.Product;
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
                .article(source.getArticle())
                .isAvailable(source.getIsAvailable())
                .build();
    }
}
