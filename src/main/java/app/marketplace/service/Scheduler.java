package app.marketplace.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import app.marketplace.entity.Product;
import app.marketplace.repository.ProductRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
        List<Product> allProducts = productRepository.getAllProductsForLock();
        for (Product product : allProducts) {
            product.setPrice(product.getPrice().multiply(new BigDecimal("1.1")));
        }
        productRepository.saveAll(allProducts);
        sleep(30000);
        log.info("Price Increase");
    }
}
