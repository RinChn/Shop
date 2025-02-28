package marketplace.converter.order;

import marketplace.controller.request.OrderRequestSetStatus;
import marketplace.event.type.CancelledOrderEvent;
import marketplace.util.OrderStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CancelEventToOcSetStatusConverter implements Converter<CancelledOrderEvent, OrderRequestSetStatus> {
    @Override
    public OrderRequestSetStatus convert(CancelledOrderEvent source) {
        return OrderRequestSetStatus.builder()
                .status(OrderStatus.CANCELLED)
                .orderNumber(source.getOrderNumber())
                .build();
    }
}
