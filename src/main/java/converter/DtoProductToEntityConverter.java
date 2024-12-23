package converter;

import dto.ProductRequest;
import entities.Category;
import entities.Product;
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
