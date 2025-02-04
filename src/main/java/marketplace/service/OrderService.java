package marketplace.service;

import marketplace.controller.request.OrderCompositionRequest;
import marketplace.controller.response.OrderCompositionResponse;
import marketplace.controller.request.OrderRequestSetStatus;
import marketplace.controller.response.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse addToOrder(Integer number, OrderCompositionRequest source);
    OrderResponse setStatus(OrderRequestSetStatus request);
    UUID deleteOrder(Integer orderNumber);
    List<OrderResponse> getAllOrdersOfUser();
    List<OrderCompositionResponse> getTheOrderDetails(Integer orderNumber);
    OrderResponse removeProductsFromOrder(Integer orderNumber, OrderCompositionRequest source);
    OrderResponse createOrder(OrderCompositionRequest source);
}
