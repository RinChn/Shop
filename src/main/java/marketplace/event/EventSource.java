package marketplace.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import marketplace.event.type.CancelledOrderEvent;
import marketplace.event.type.CreateOrderEvent;
import marketplace.event.type.SetStatusOrderEvent;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "event"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateOrderEvent.class, name = "CREATE_ORDER"),
        @JsonSubTypes.Type(value = CancelledOrderEvent.class, name = "CANCELLED_ORDER"),
        @JsonSubTypes.Type(value = SetStatusOrderEvent.class, name = "ANY_STATUS_ORDER")
})
public interface EventSource {
    EventName getEvent();
}
