package marketplace.event.handler;

import marketplace.controller.response.OrderResponse;
import marketplace.event.EventName;
import marketplace.event.EventSource;

public interface EventHandler<T extends EventSource> {
    OrderResponse handle(T event);
    EventName getEventName();
}