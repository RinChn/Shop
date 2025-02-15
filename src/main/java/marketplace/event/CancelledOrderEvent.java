package marketplace.event;

public class CancelledOrderEvent implements EventSource {
    private EventName event;
    private Integer orderNumber;

    @Override
    public EventName getEvent() {
        return event;
    }
}
