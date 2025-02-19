package marketplace.converter.order;

import marketplace.controller.response.OrderAndTinResponse;
import marketplace.entity.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderToResponseAndTinConverter implements Converter<Order, OrderAndTinResponse> {
    @Override
    public OrderAndTinResponse convert(Order source) {
        return OrderAndTinResponse.builder()
                .price(source.getPrice())
                .emailCustomer(source.getCustomer().getEmail())
                .number(source.getNumber())
                .status(source.getStatus())
                .build();
    }
}
