package marketplace.event.type;

import lombok.Getter;
import lombok.Setter;
import marketplace.event.EventName;
import marketplace.event.EventSource;

@Getter
@Setter
public class CancelledOrderEvent implements EventSource {
    private final EventName event = EventName.CANCELLED_ORDER;
    private Integer orderNumber;

    @Override
    public EventName getEvent() {
        return event;
    }
}
