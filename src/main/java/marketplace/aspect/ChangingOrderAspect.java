package marketplace.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.controller.request.OrderRequestSetStatus;
import marketplace.entity.Order;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import marketplace.repository.OrderRepository;
import marketplace.util.OrderStatus;
import marketplace.util.UserHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class ChangingOrderAspect {
    private final OrderRepository orderRepository;
    private final UserHandler userHandler;
    private final ThreadLocal<Order> orderContext = new ThreadLocal<>();

    @Before("@annotation(marketplace.aspect.ChangingOrder)")
    public void checkOrderStatus(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Integer orderNumber = 0;
        if (args[0] instanceof Integer) {
            orderNumber = (Integer) args[0];
            log.info("Integer: Order number: {}", orderNumber);
        } else if (args[0] instanceof OrderRequestSetStatus) {
            OrderRequestSetStatus request = (OrderRequestSetStatus) args[0];
            orderNumber = request.getOrderNumber();
            log.info("OrderRequestSetStatus: Order number: {}", orderNumber);
        }
        Order order = orderRepository.findOrderByNumber(orderNumber, userHandler.getCurrentUser())
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTEN_ORDER));
        log.info("Order with UUID {}", order.getId());
        OrderStatus status = order.getStatus();
        if (args[0] instanceof OrderRequestSetStatus && status.equals(OrderStatus.CANCELLED)) {
            throw new ApplicationException(ErrorType.SET_STATUS_FOR_CANCELLED_ORDER);
        } else if (args[0] instanceof Integer && !status.equals(OrderStatus.CREATED)) {
            throw new ApplicationException(ErrorType.SET_NOT_CREATED_ORDER);
        }
        orderContext.set(order);
    }

    @AfterThrowing("@annotation(marketplace.aspect.ChangingOrder)")
    public void clearContext() {
        orderContext.remove();
    }

    public Order getOrderFromContext() {
        return orderContext.get();
    }
}
