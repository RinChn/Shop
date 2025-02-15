package marketplace.event;

import marketplace.util.OrderStatus;

public class SetStatusOrderEvent implements EventSource {
    private EventName event;
    private Integer orderNumber;
    private OrderStatus status;

    @Override
    public EventName getEvent() {
        return event;
    }
}
