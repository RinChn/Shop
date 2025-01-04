package marketplace.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.repository.ProductRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Slf4j
@EnableAsync
@EnableScheduling
@ConditionalOnProperty(value = "scheduler.enabled", matchIfMissing = false)
@RequiredArgsConstructor
public class Scheduler {

    private final ProductRepository productRepository;

    @Scheduled(fixedRateString = "${scheduler.period}")
    @Transactional
    @Async
    void priceIncrease() {
        productRepository.increasePrices();
        log.info("Price increased");
    }
}
