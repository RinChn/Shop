package marketplace.converter.product;

import marketplace.controller.request.ProductRequestUpdate;
import marketplace.entity.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestUpdateToProductConverter implements Converter<ProductRequestUpdate, Product> {
    @Override
    public Product convert(ProductRequestUpdate source) {
        return Product.builder()
                .name(source.getName())
                .description(source.getDescription())
                .quantity(source.getQuantity())
                .price(source.getPrice())
                .categories(source.getCategories())
                .isAvailable(source.getIsAvailable())
                .build();

    }
}
