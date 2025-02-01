package marketplace.converter;

import marketplace.dto.OrderRequestSetStatus;
import marketplace.entity.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestSetStatusToOrderConverter implements Converter<OrderRequestSetStatus, Order> {
    @Override
    public Order convert(OrderRequestSetStatus source) {
        return Order.builder()
                .status(source.getStatus())
                .build();
    }
}
