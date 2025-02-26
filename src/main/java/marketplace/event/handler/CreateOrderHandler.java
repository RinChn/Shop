package marketplace.event.handler;

import lombok.RequiredArgsConstructor;
import marketplace.controller.request.OrderCompositionRequest;
import marketplace.controller.response.OrderResponse;
import marketplace.event.EventName;
import marketplace.event.type.CreateOrderEvent;
import marketplace.service.OrderService;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrderHandler implements EventHandler<CreateOrderEvent> {
    private final OrderService orderService;
    private final ConversionService conversionService;

    @Override
    public OrderResponse handle(CreateOrderEvent event) {
        return orderService.createOrder(event.getIdempotencyKey(),
                conversionService.convert(event, OrderCompositionRequest.class),
                event.getEmailConsumer());
    }

    @Override
    public EventName getEventName() {
        return EventName.CREATE_ORDER;
    }
}
