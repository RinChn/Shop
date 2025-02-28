package marketplace.converter.product;

import marketplace.controller.request.ProductRequestCreate;
import marketplace.entity.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestCreateToProductConverter implements Converter<ProductRequestCreate, Product> {
    @Override
    public Product convert(ProductRequestCreate source) {
        return Product.builder()
                .article(source.getArticle())
                .name(source.getName())
                .description(source.getDescription())
                .quantity(source.getQuantity())
                .price(source.getPrice())
                .categories(source.getCategories())
                .isAvailable(source.getIsAvailable())
                .build();

    }
}
