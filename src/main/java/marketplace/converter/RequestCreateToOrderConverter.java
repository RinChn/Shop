package marketplace.converter;

import marketplace.dto.OrderRequestCreate;
import marketplace.entity.Order;
import marketplace.entity.User;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import marketplace.repository.OrderRepository;
import marketplace.repository.UserRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestCreateToOrderConverter implements Converter<OrderRequestCreate, Order> {
    UserRepository userRepository;
    OrderRepository orderRepository;

    @Override
    public Order convert(OrderRequestCreate source) {
        User user = userRepository.findByEmail(source.getEmailCustomer())
                .orElseThrow(() -> new ApplicationException(ErrorType.UNREGISTERED_MAIL));
        Integer numberNewOrder = orderRepository.getNumberOfUserOrders(user).intValue() + 1;

        return Order.builder()
                .customer(user)
                .number(numberNewOrder)
                .build();
    }
}
