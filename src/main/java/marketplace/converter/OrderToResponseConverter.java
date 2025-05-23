package marketplace.converter;

import marketplace.controller.response.OrderResponse;
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
                .emailCustomer(source.getCustomer().getEmail())
                .build();
    }
}
