package marketplace.service;

import marketplace.aspect.Timer;
import marketplace.dto.OrderCompositionRequest;
import marketplace.dto.OrderCompositionResponse;
import marketplace.dto.OrderRequestSetStatus;
import marketplace.dto.OrderResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse addToOrder(OrderCompositionRequest source);
    OrderResponse setStatus(OrderRequestSetStatus request);
    UUID deleteOrder(Integer orderNumber);
    List<OrderResponse> getAllOrdersOfUser();
    List<OrderCompositionResponse> getTheOrderDetails(Integer orderNumber);
    OrderResponse removeProductsFromOrder(OrderCompositionRequest source, Integer orderNumber);
}
