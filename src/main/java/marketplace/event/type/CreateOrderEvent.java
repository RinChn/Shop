package marketplace.event.type;

import lombok.Getter;
import lombok.Setter;
import marketplace.event.EventName;
import marketplace.event.EventSource;

@Getter
@Setter
public class CreateOrderEvent implements EventSource {
    private final EventName event = EventName.CREATE_ORDER;
    private Integer productArticle;
    private Integer productQuantity;
    private String emailConsumer;

    @Override
    public EventName getEvent() {
        return event;
    }
}
