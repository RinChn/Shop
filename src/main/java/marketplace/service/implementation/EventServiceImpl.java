package marketplace.service.implementation;

import lombok.RequiredArgsConstructor;
import marketplace.controller.request.OrderCompositionRequest;
import marketplace.controller.request.OrderRequestSetStatus;
import marketplace.controller.response.OrderResponse;
import marketplace.event.EventSource;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import marketplace.service.EventService;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final OrderServiceImpl orderService;
    private final ConversionService conversionService;

    @Override
    @Transactional
    public OrderResponse recognizeEvent(EventSource event) {
        OrderResponse order;
        switch (event.getEvent()) {
            case CREATE_ORDER -> order = orderService.createOrder(conversionService.convert(event, OrderCompositionRequest.class));
            case CANCELLED_ORDER, ANY_STATUS_ORDER -> order = orderService.setStatus(conversionService.convert(event,
                    OrderRequestSetStatus.class));
            default -> throw new ApplicationException(ErrorType.INCORRECT_EVENT_NAME);
        }
        return order;
    }
}
