package marketplace.event.handler;

import lombok.RequiredArgsConstructor;
import marketplace.controller.request.OrderRequestSetStatus;
import marketplace.controller.response.OrderResponse;
import marketplace.event.EventName;
import marketplace.event.type.CancelledOrderEvent;
import marketplace.service.OrderService;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancelOrderHandler implements EventHandler<CancelledOrderEvent> {
    private final OrderService orderService;
    private final ConversionService conversionService;

    @Override
    public OrderResponse handle(CancelledOrderEvent event) {
        return orderService.setStatus(conversionService.convert(event, OrderRequestSetStatus.class),
                event.getEmailConsumer());
    }

    @Override
    public EventName getEventName() {
        return EventName.CANCELLED_ORDER;
    }
}