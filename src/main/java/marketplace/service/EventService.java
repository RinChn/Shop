package marketplace.service;

import marketplace.controller.response.OrderResponse;
import marketplace.event.EventSource;

public interface EventService {
    OrderResponse recognizeEvent(EventSource event);
}
