package marketplace.converter.order;

import marketplace.controller.request.OrderRequestSetStatus;
import marketplace.event.type.SetStatusOrderEvent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AnyStatusEventToOcSetStatusConverter implements Converter<SetStatusOrderEvent, OrderRequestSetStatus> {
    @Override
    public OrderRequestSetStatus convert(SetStatusOrderEvent source) {
        return OrderRequestSetStatus.builder()
                .status(source.getStatus())
                .orderNumber(source.getOrderNumber())
                .build();
    }
}
