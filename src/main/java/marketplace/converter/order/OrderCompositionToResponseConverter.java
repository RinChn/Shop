package marketplace.converter.order;

import marketplace.controller.response.OrderCompositionResponse;
import marketplace.entity.OrderComposition;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderCompositionToResponseConverter implements Converter<OrderComposition, OrderCompositionResponse> {

    @Override
    public OrderCompositionResponse convert(OrderComposition source) {
        return OrderCompositionResponse.builder()
                .productArticle(source.getProduct().getArticle())
                .productQuantity(source.getProductQuantity())
                .productPrice(source.getPrice())
                .build();
    }
}
