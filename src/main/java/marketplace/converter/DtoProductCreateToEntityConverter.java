package marketplace.converter;

import marketplace.dto.ProductRequestCreate;
import marketplace.entity.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DtoProductCreateToEntityConverter implements Converter<ProductRequestCreate, Product> {
    @Override
    public Product convert(ProductRequestCreate source) {
        return Product.builder()
                .name(source.getName())
                .description(source.getDescription())
                .quantity(source.getQuantity())
                .price(source.getPrice())
                .categories(source.getCategories())
                .build();

    }
}
