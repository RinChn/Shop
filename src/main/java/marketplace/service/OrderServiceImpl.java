package marketplace.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.aspect.Timer;
import marketplace.dto.*;
import marketplace.entity.*;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import marketplace.repository.OrderCompositionRepository;
import marketplace.repository.OrderRepository;
import marketplace.repository.ProductRepository;
import marketplace.util.OrderStatus;
import marketplace.util.UserHandler;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;
    private final OrderCompositionRepository orderCompositionRepository;
    private final ConversionService conversionService;
    public final UserHandler userHandler;

    @Override
    @Transactional
    @Timer
    public OrderResponse addToOrder(OrderCompositionRequest source) {
        Integer quantity = source.getProductQuantity();

        User customer = userHandler.getCurrentUser();

        Order order = orderRepository
                .findOrderOfCustomerByStatus(customer, OrderStatus.CREATED)
                .orElse(Order.builder()
                        .customer(customer)
                        .number(orderRepository.getNumberOfUserOrders(customer).intValue() + 1)
                        .build());
        Product product = productService.bookProduct(source.getProductArticle(), quantity);

        OrderCompositionId orderCompositionId = OrderCompositionId.builder()
                .orderId(order.getId())
                .productId(product.getId())
                .build();

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        OrderComposition orderComposition = orderCompositionRepository
                .findById(orderCompositionId)
                .orElse(OrderComposition.builder()
                        .id(orderCompositionId)
                        .order(order)
                        .product(product)
                        .price(totalPrice)
                        .productQuantity(quantity)
                        .build());
        BigDecimal currentCompositionPrice = orderComposition.getPrice();
        BigDecimal currentCompositionQuantity = BigDecimal.valueOf(orderComposition.getProductQuantity());
        BigDecimal productPriceInComposition = currentCompositionPrice
                .divide(currentCompositionQuantity, 2, RoundingMode.HALF_UP);
        if (product.getPrice().compareTo(productPriceInComposition) != 0) {
            log.info("Product price is not equal to product price in composition: {}", productPriceInComposition);
            totalPrice = productPriceInComposition.multiply(BigDecimal.valueOf(quantity));
            orderComposition.setPrice(currentCompositionPrice.add(totalPrice));
            orderComposition.setProductQuantity(quantity + orderComposition.getProductQuantity());
        }
        log.info("Old order price: {}", order.getPrice());
        order.setPrice(totalPrice.add(order.getPrice()));
        orderRepository.save(order);
        log.info("New order price: {}", order.getPrice());

        orderCompositionRepository.save(orderComposition);
        log.info("New order composition id: {}", orderCompositionId);

        return conversionService.convert(order, OrderResponse.class);
    }

    @Transactional
    @Timer
    @Override
    public OrderResponse setStatus(OrderRequestSetStatus request) {
        User customer = userHandler.getCurrentUser();
        Order order = orderRepository.findOrderByNumber(request.getOrderNumber(), customer)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTEN_ORDER));
        if (request.getStatus().equals(OrderStatus.CREATED) && orderRepository
                .findOrderOfCustomerByStatus(customer, OrderStatus.CREATED).isPresent()) {
            log.error("Open order already exists");
            throw new ApplicationException(ErrorType.OPEN_ORDER_ALREADY_EXISTS);
        }
        order.setStatus(request.getStatus());
        orderRepository.save(order);
        log.info("New order {} status: {}", order.getId(), order.getStatus());
        return conversionService.convert(order, OrderResponse.class);
    }

    @Transactional
    @Timer
    @Override
    public UUID deleteOrder(Integer orderNumber) {
        Order order = orderRepository.findOrderByNumber(orderNumber, userHandler.getCurrentUser())
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTEN_ORDER));
        orderCompositionRepository.deleteAll(orderCompositionRepository.findCompositionsOfOrder(order));
        orderRepository.delete(order);
        log.info("Order {} deleted", orderNumber);
        return order.getId();
    }

    @Transactional(readOnly = true)
    @Timer
    @Override
    public List<OrderResponse> getAllOrdersOfUser() {
        return orderRepository.findAllOrdersOfCustomer(userHandler.getCurrentUser())
                .stream()
                .map(order -> conversionService.convert(order, OrderResponse.class))
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    @Timer
    @Override
    public List<OrderCompositionResponse> getTheOrderDetails(Integer orderNumber) {
        Order order = orderRepository.findOrderByNumber(orderNumber, userHandler.getCurrentUser())
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTEN_ORDER));
        log.info("Order id: {}", order.getId());
        return orderCompositionRepository.findCompositionsOfOrder(order)
                .stream()
                .map(composition ->
                        conversionService.convert(composition, OrderCompositionResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Timer
    @Override
    public OrderResponse removeProductsFromOrder(OrderCompositionRequest source, Integer orderNumber) {

        Order order = orderRepository.findOrderByNumber(orderNumber, userHandler.getCurrentUser())
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTEN_ORDER));
        Product product = productRepository.findByArticle(source.getProductArticle())
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_ARTICLE));

        OrderComposition orderComposition = orderCompositionRepository.findById(OrderCompositionId.builder()
                        .orderId(order.getId())
                        .productId(product.getId())
                        .build())
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_PRODUCT_IN_ORDER));

        Integer currentProductQuantityInOrder = orderComposition.getProductQuantity();
        Integer quantityProductDeleting = source.getProductQuantity();
        log.info("Current product quantity in order: {}", currentProductQuantityInOrder);
        log.info("Current order price: {}", order.getPrice());

        BigDecimal productPrice = orderComposition.getPrice()
                .divide(BigDecimal.valueOf(currentProductQuantityInOrder), 2, RoundingMode.HALF_UP);
        BigDecimal totalPriceProductDeleting;
        if (currentProductQuantityInOrder <= quantityProductDeleting) {
            orderCompositionRepository.delete(orderComposition);
            totalPriceProductDeleting = productPrice
                    .multiply(BigDecimal.valueOf(currentProductQuantityInOrder));
            log.info("This product is no longer in the order.");
        } else {
            orderComposition.setProductQuantity(currentProductQuantityInOrder - quantityProductDeleting);
            totalPriceProductDeleting = productPrice.multiply(BigDecimal.valueOf(quantityProductDeleting));
            orderComposition.setPrice(orderComposition.getPrice().subtract(totalPriceProductDeleting));
            orderCompositionRepository.save(orderComposition);
            log.info("There are {} items with article {} left with a price of {}",
                    orderComposition.getProductQuantity(),
                    product.getArticle(),
                    orderComposition.getPrice());
        }

        order.setPrice(order.getPrice().subtract(totalPriceProductDeleting));
        orderRepository.save(order);
        log.info("New order price: {}", order.getPrice());

        return conversionService.convert(order, OrderResponse.class);
    }

}
