package marketplace.event;

public class CreateOrderEvent implements EventSource{
    private EventName event;
    private Integer productArticle;
    private Integer productQuantity;

    @Override
    public EventName getEvent() {
        return event;
    }
}
