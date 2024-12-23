package converter;

import dto.ProductRequest;
import entity.Category;
import entity.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DtoProductToEntityConverter implements Converter<ProductRequest, Product> {
    @Override
    public Product convert(ProductRequest source) {
        return Product.builder()
                .article(source.getArticle())
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
