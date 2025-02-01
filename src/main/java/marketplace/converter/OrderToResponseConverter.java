package marketplace.converter;

import marketplace.dto.OrderResponse;
import marketplace.entity.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderToResponseConverter implements Converter<Order, OrderResponse> {

    @Override
    public OrderResponse convert(Order source) {
        return OrderResponse.builder()
                .number(source.getNumber())
                .price(source.getPrice())
                .status(source.getStatus())
                .email_customer(source.getCustomer().getEmail())
                .build();
    }
}
