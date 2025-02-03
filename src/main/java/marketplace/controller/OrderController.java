package marketplace.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.controller.request.OrderCompositionRequest;
import marketplace.controller.request.OrderRequestSetStatus;
import marketplace.controller.response.OrderCompositionResponse;
import marketplace.controller.response.OrderResponse;
import marketplace.service.OrderServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderCompositionRequest orderCompositionRequest) {
        return orderService.createOrder(orderCompositionRequest);
    }

    @PostMapping("/{number}")
    public OrderResponse addProductToOrder(@PathVariable("number") Integer number,
            @RequestBody OrderCompositionRequest orderCompositionRequest) {
        return orderService.addToOrder(number, orderCompositionRequest);
    }

    @PutMapping
    public OrderResponse setStatusToOrder(@RequestBody OrderRequestSetStatus orderRequestSetStatus) {
        return orderService.setStatus(orderRequestSetStatus);
    }

    @DeleteMapping("/{number}")
    public UUID deleteOrder(@PathVariable("number") Integer orderNumber) {
        return orderService.deleteOrder(orderNumber);
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrdersOfUser();
    }

    @GetMapping("/{number}")
    public List<OrderCompositionResponse> getTheOrderDetails(@PathVariable("number") Integer orderNumber) {
        return orderService.getTheOrderDetails(orderNumber);
    }

    @DeleteMapping("/{number}/product")
    public OrderResponse removeProductsFromOrder(@RequestBody OrderCompositionRequest orderCompositionRequest,
                                                 @PathVariable("number") Integer orderNumber) {
        return orderService.removeProductsFromOrder(orderCompositionRequest, orderNumber);
    }

}
