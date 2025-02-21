package marketplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.controller.request.OrderCompositionRequest;
import marketplace.controller.request.OrderRequestSetStatus;
import marketplace.controller.response.OrderCompositionResponse;
import marketplace.controller.response.OrderResponse;
import marketplace.dto.OrderAndDetailsDto;
import marketplace.event.EventSource;
import marketplace.service.implementation.EventServiceImpl;
import marketplace.service.implementation.OrderServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;
    private final EventServiceImpl eventService;

    @PostMapping
    public OrderResponse createOrder(@Valid @RequestBody OrderCompositionRequest orderCompositionRequest) {
        return orderService.createOrder(orderCompositionRequest, null);
    }

    @PostMapping("/{number}")
    public OrderResponse addProductToOrder(@PathVariable("number") Integer number,
            @RequestBody OrderCompositionRequest orderCompositionRequest) {
        return orderService.addToOrder(number, orderCompositionRequest);
    }

    @PutMapping
    public OrderResponse setStatusToOrder(@RequestBody OrderRequestSetStatus orderRequestSetStatus) {
        return orderService.setStatus(orderRequestSetStatus, null);
    }

    @DeleteMapping("/{number}")
    public UUID deleteOrder(@PathVariable("number") Integer orderNumber) {
        return orderService.deleteOrder(orderNumber);
    }

    @GetMapping
    public List<OrderAndDetailsDto> getAllOrders() {
        return orderService.getAllOrdersOfUser();
    }

    @GetMapping("/{number}")
    public List<OrderCompositionResponse> getTheOrderDetails(@PathVariable("number") Integer orderNumber) {
        return orderService.getTheOrderDetails(orderNumber);
    }

    @DeleteMapping("/{number}/product")
    public OrderResponse removeProductsFromOrder(@Valid @RequestBody OrderCompositionRequest orderCompositionRequest,
                                                 @PathVariable("number") Integer orderNumber) {
        return orderService.removeProductsFromOrder(orderNumber, orderCompositionRequest);
    }

    @PostMapping("/event")
    public OrderResponse setStatus(@RequestBody EventSource eventSource) {
        return eventService.recognizeEvent(eventSource);
    }

}
