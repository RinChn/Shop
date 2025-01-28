package marketplace.converter;

import marketplace.dto.OrderRequestUpdate;
import marketplace.entity.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestUpdateToOrderConverter implements Converter<OrderRequestUpdate, Order> {
    @Override
    public Order convert(OrderRequestUpdate source) {
        return Order.builder()
                .status(source.getStatus())
                .build();
    }
}
