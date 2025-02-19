package marketplace.event.type;

import lombok.Getter;
import lombok.Setter;
import marketplace.event.EventName;
import marketplace.event.EventSource;
import marketplace.util.OrderStatus;

@Getter
@Setter
public class SetStatusOrderEvent implements EventSource {
    private final EventName event = EventName.ANY_STATUS_ORDER;
    private Integer orderNumber;
    private OrderStatus status;

    @Override
    public EventName getEvent() {
        return event;
    }
}
