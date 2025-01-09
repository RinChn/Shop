package marketplace.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.repository.ProductRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Thread.sleep;

@Configuration
@Slf4j
@EnableScheduling
@ConditionalOnProperty(value = "scheduler.enabled", matchIfMissing = false)
@RequiredArgsConstructor
public class Scheduler {

    private final ProductRepository productRepository;

    @Scheduled(fixedRateString = "${scheduler.period}")
    @Transactional
    void priceIncrease() throws InterruptedException {
        productRepository.increasePrices();
        log.info("Price increased");
        sleep(30000);
    }
}
