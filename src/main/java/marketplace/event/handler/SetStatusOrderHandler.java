package marketplace.event.handler;

import lombok.RequiredArgsConstructor;
import marketplace.controller.request.OrderRequestSetStatus;
import marketplace.controller.response.OrderResponse;
import marketplace.event.EventName;
import marketplace.event.type.SetStatusOrderEvent;
import marketplace.service.OrderService;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SetStatusOrderHandler implements EventHandler<SetStatusOrderEvent> {
    private final OrderService orderService;
    private final ConversionService conversionService;

    @Override
    public OrderResponse handle(SetStatusOrderEvent event) {
        return orderService.setStatus(conversionService.convert(event, OrderRequestSetStatus.class),
                event.getEmailConsumer());
    }

    @Override
    public EventName getEventName() {
        return EventName.ANY_STATUS_ORDER;
    }
}
