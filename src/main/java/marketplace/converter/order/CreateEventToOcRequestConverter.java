package marketplace.converter.order;

import marketplace.controller.request.OrderCompositionRequest;
import marketplace.event.type.CreateOrderEvent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateEventToOcRequestConverter implements Converter<CreateOrderEvent, OrderCompositionRequest> {
    @Override
    public OrderCompositionRequest convert(CreateOrderEvent source) {
        return OrderCompositionRequest.builder()
                .productQuantity(source.getProductQuantity())
                .productArticle(source.getProductArticle())
                .build();
    }
}
