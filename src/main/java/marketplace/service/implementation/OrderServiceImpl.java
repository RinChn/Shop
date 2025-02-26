package marketplace.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.aspect.Timer;
import marketplace.controller.request.OrderCompositionRequest;
import marketplace.controller.request.OrderRequestSetStatus;
import marketplace.controller.response.OrderCompositionResponse;
import marketplace.controller.response.OrderResponse;
import marketplace.dto.ComponentOfOrderDto;
import marketplace.dto.OrderAndDetailsDto;
import marketplace.entity.*;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import marketplace.repository.OrderCompositionRepository;
import org.springframework.cache.annotation.Cacheable;
import marketplace.repository.OrderRepository;
import marketplace.repository.ProductRepository;
import marketplace.service.OrderService;
import marketplace.util.OrderStatus;
import marketplace.util.UserHandler;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
    private final UserHandler userHandler;
    private final RedisTemplate<UUID, UUID> redisTemplate;

    @Override
    @Transactional
    @Timer
    @Cacheable(value = "ordersCache", key = "#idempotencyKey")
    public OrderResponse createOrder(UUID idempotencyKey, OrderCompositionRequest source, String email) {

        UUID existingOrderId = redisTemplate.opsForValue().get(idempotencyKey);
        log.info("Create Order Request");
        if (existingOrderId != null) {
            log.info("Order found in cache: {}", existingOrderId);
            return conversionService.convert(orderRepository.findById(existingOrderId).get(), OrderResponse.class);
        }
        User customer;
        if (email == null) {
            customer = userHandler.getCurrentUser();
        } else {
            log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! {}", email);
            customer = userHandler.getUserByEmail(email);
        }
        Product product = productService.bookProduct(source.getProductArticle(), source.getProductQuantity());
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(source.getProductQuantity()));
        Long lastNumber = orderRepository.getLastNumberOfUserOrders(customer);
        if (lastNumber == null) {
            lastNumber = 0L;
        }
        Order order = Order.builder()
                .number(lastNumber.intValue() + 1)
                .price(totalPrice)
                .customer(customer)
                .build();
        orderRepository.save(order);
        log.info("Order created {} for user {}", order.getNumber(), order.getCustomer().getEmail());
        OrderCompositionId orderCompositionId = OrderCompositionId.builder()
                .orderId(order.getId())
                .productId(product.getId())
                .build();
        OrderComposition orderComposition = OrderComposition
                .builder()
                .id(orderCompositionId)
                .order(order)
                .product(product)
                .price(totalPrice)
                .productQuantity(source.getProductQuantity())
                .build();
        orderCompositionRepository.save(orderComposition);
        log.info("Order created {} for user {}", order.getNumber(), order.getCustomer().getEmail());
        redisTemplate.opsForValue().set(idempotencyKey, order.getId(), Duration.ofMinutes(10));
        return conversionService.convert(order, OrderResponse.class);
    }

    @Override
    @Transactional
    @Timer
    public OrderResponse addToOrder(Integer number, OrderCompositionRequest source) {
        Order order = checkOrderStatusToChangeComposition(number, userHandler.getCurrentUser());
        Integer quantity = source.getProductQuantity();
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
            log.warn("Product price is not equal to product price in composition: {}", productPriceInComposition);
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
    public OrderResponse setStatus(OrderRequestSetStatus request, String email) {
        User customer;
        if (email == null) {
            customer = userHandler.getCurrentUser();
        } else {
            customer = userHandler.getUserByEmail(email);
        }
        Order order = orderRepository.findOrderByNumber(request.getOrderNumber(), customer)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTEN_ORDER));
        order.setStatus(request.getStatus());
        orderRepository.save(order);

        if (request.getStatus().equals(OrderStatus.CANCELLED)) {
            returnOfProducts(orderCompositionRepository.findCompositionsOfOrder(order));
        }

        log.info("New order {} status: {}", order.getId(), order.getStatus());
        return conversionService.convert(order, OrderResponse.class);
    }

    private void returnOfProducts(List<OrderComposition> ordersComposition) {
        productService.returnOfProductsToWarehouse(ordersComposition.stream()
                .collect(Collectors.toMap(
                        OrderComposition::getProduct,
                        OrderComposition::getProductQuantity)));
        log.info("Products from order have been returned");
    }

    @Transactional
    @Timer
    @Override
    public UUID deleteOrder(Integer orderNumber) {
        Order order = orderRepository.findOrderByNumber(orderNumber, userHandler.getCurrentUser())
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTEN_ORDER));
        List<OrderComposition> allOrdersComposition = orderCompositionRepository.findCompositionsOfOrder(order);
        if (!order.getStatus().equals(OrderStatus.CANCELLED)) {
            returnOfProducts(allOrdersComposition);
        }
        orderCompositionRepository.deleteAll(allOrdersComposition);
        orderRepository.delete(order);
        log.info("Order {} deleted", order.getId());
        return order.getId();
    }

    @Transactional(readOnly = true)
    @Timer
    @Override
    public List<OrderAndDetailsDto> getAllOrdersOfUser() {
        List<Order> orders = orderRepository.findAllOrdersOfCustomer(userHandler.getCurrentUser());
        List<OrderAndDetailsDto> orderAndDetailsDto = new ArrayList<>();
        for (Order order : orders) {
            List<ComponentOfOrderDto> allCompositions = orderCompositionRepository
                    .findCompositionsOfOrder(order)
                    .stream()
                    .map(composition ->
                            conversionService.convert(composition, ComponentOfOrderDto.class))
                    .toList();
            orderAndDetailsDto.add(OrderAndDetailsDto.builder()
                    .order(conversionService.convert(order, OrderResponse.class))
                    .components(allCompositions)
                    .build());
        }
        log.info("Information about {} orders was received", orderAndDetailsDto.size());
        return orderAndDetailsDto;

    }

    @Transactional(readOnly = true)
    @Timer
    @Override
    public List<OrderCompositionResponse> getTheOrderDetails(Integer orderNumber) {
        Order order = orderRepository.findOrderByNumber(orderNumber, userHandler.getCurrentUser())
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTEN_ORDER));
        log.info("Get info about order {}", order.getId());
        return orderCompositionRepository.findCompositionsOfOrder(order)
                .stream()
                .map(composition ->
                        conversionService.convert(composition, OrderCompositionResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Timer
    @Override
    public OrderResponse removeProductsFromOrder(Integer orderNumber, OrderCompositionRequest source) {
        Order order = checkOrderStatusToChangeComposition(orderNumber, userHandler.getCurrentUser());
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
            product.setQuantity(product.getQuantity() + currentProductQuantityInOrder);
        } else {
            orderComposition.setProductQuantity(currentProductQuantityInOrder - quantityProductDeleting);
            totalPriceProductDeleting = productPrice.multiply(BigDecimal.valueOf(quantityProductDeleting));
            orderComposition.setPrice(orderComposition.getPrice().subtract(totalPriceProductDeleting));
            orderCompositionRepository.save(orderComposition);
            log.info("There are {} items with article {} left with a price of {}",
                    orderComposition.getProductQuantity(),
                    product.getArticle(),
                    orderComposition.getPrice());
            product.setQuantity(product.getQuantity() + quantityProductDeleting);
        }

        order.setPrice(order.getPrice().subtract(totalPriceProductDeleting));
        orderRepository.save(order);
        log.info("New order price: {}", order.getPrice());

        productRepository.save(product);

        return conversionService.convert(order, OrderResponse.class);
    }

    private Order checkOrderStatusToChangeComposition(Integer orderNumber, User user) {
        Order order = orderRepository.findOrderByNumber(orderNumber, user)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTEN_ORDER));
        log.info("Order with UUID {}", order.getId());
        OrderStatus status = order.getStatus();
        if (!status.equals(OrderStatus.CREATED)) {
            throw new ApplicationException(ErrorType.SET_NOT_CREATED_ORDER);
        }
        return order;
    }

}
