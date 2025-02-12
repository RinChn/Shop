package marketplace.exchange;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class ExchangeTaxHandler {
    private final TaxFeignClient taxFeignClient;

    @Cacheable(value = "exchangeCache", key = "#emails")
    public List<String> getTins(List<String> emails) {
        try {
            log.info("Successfully get tins for {}", emails);
            return taxFeignClient.getTinAboutUsers(emails);
        } catch (FeignException ex) {
            log.error("Error getting tins for {}", emails);
            return Collections.emptyList();
        }
    }
}
