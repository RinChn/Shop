package marketplace.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.dto.*;
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
    public OrderResponse addProductToOrder(@RequestBody OrderCompositionRequest orderCompositionRequest) {
        return orderService.addToOrder(orderCompositionRequest);
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
