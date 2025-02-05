package marketplace.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.aspect.Timer;
import marketplace.controller.response.OrderResponse;
import marketplace.dto.UserAndOrdersDto;
import marketplace.dto.UserDto;
import marketplace.entity.Order;
import marketplace.entity.User;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import marketplace.repository.OrderRepository;
import marketplace.repository.UserRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ConversionService conversionService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    @Timer
    public UUID register(UserDto newUser) {
        User user = conversionService.convert(newUser, User.class);
        userRepository.findByEmail(user.getEmail())
                .ifPresent(result -> {
                    throw new ApplicationException(ErrorType.DUPLICATE);
        });
        userRepository.save(user);
        log.info("User registered: {}", user.getEmail().toString());
        return user.getId();
    }

    @Transactional(readOnly = true)
    @Timer
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> conversionService.convert(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Timer
    public List<UserAndOrdersDto> getUserAndOrders() {
        List<Order> orders = orderRepository.findAllFetch();

        Map<User, List<OrderResponse>> userOrdersMap = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getCustomer,
                        Collectors.mapping(
                                order -> conversionService.convert(order, OrderResponse.class),
                                Collectors.toList(
                        )
                )));
        log.info("Get all user orders");
        return userOrdersMap.entrySet().stream()
                .map(entry -> UserAndOrdersDto.builder()
                        .email(entry.getKey().getEmail())
                        .orders(entry.getValue())
                        .build())
                .toList();
    }
}
