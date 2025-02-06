package marketplace.converter;

import marketplace.dto.DetailedPartOfOrderDto;
import marketplace.entity.OrderComposition;
import marketplace.entity.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderCompositionToDetailedPartConverter  implements Converter<OrderComposition, DetailedPartOfOrderDto> {

    @Override
    public DetailedPartOfOrderDto convert(OrderComposition source) {
        Product product = source.getProduct();
        return DetailedPartOfOrderDto.builder()
                .article(product.getArticle())
                .name(product.getName())
                .quantityInOrder(source.getProductQuantity())
                .category(product.getCategories())
                .description(product.getDescription())
                .totalPriceForOrder(source.getPrice())
                .build();
    }
}
