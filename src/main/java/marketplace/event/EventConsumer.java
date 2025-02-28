package marketplace.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.service.implementation.EventServiceImpl;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {
    private final ObjectMapper objectMapper;
    private final EventServiceImpl eventService;

    @KafkaListener(topics = "events", groupId = "event-group-double")
    public void consume(String message) {
        try {
            log.info("Received message from Kafka: {}", message);
            EventSource event = objectMapper.readValue(message, EventSource.class);
            eventService.recognizeEvent(event);
        } catch (Exception e) {
            log.error("Error processing event: {}", message, e);
        }
    }
}