package marketplace.converter.order;

import marketplace.dto.ComponentOfOrderDto;
import marketplace.entity.OrderComposition;
import marketplace.entity.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderCompositionToDetailedPartConverter  implements Converter<OrderComposition, ComponentOfOrderDto> {

    @Override
    public ComponentOfOrderDto convert(OrderComposition source) {
        Product product = source.getProduct();
        return ComponentOfOrderDto.builder()
                .article(product.getArticle())
                .name(product.getName())
                .quantityInOrder(source.getProductQuantity())
                .category(product.getCategories())
                .description(product.getDescription())
                .totalPriceForOrder(source.getPrice())
                .build();
    }
}
