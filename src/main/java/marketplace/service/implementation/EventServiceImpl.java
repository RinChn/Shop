package marketplace.service.implementation;

import lombok.RequiredArgsConstructor;
import marketplace.controller.response.OrderResponse;
import marketplace.event.EventName;
import marketplace.event.EventSource;
import marketplace.event.handler.EventHandler;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import marketplace.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final Map<EventName, EventHandler<? extends EventSource>> eventHandlers;

    @Autowired
    public EventServiceImpl(List<EventHandler<? extends EventSource>> handlers) {
        this.eventHandlers = handlers.stream()
                .collect(Collectors.toMap(EventHandler::getEventName,
                        handler -> handler));
    }

    @Override
    @Transactional
    public OrderResponse recognizeEvent(EventSource event) {
        EventHandler handler = eventHandlers.get(event.getEvent());
        if (handler == null) {
            throw new ApplicationException(ErrorType.INCORRECT_EVENT_NAME);
        }
        return handler.handle(event);
    }
}
