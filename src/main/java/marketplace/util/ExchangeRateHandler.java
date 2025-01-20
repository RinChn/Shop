package marketplace.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Component
@Slf4j
public class ExchangeRateHandler {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.file-names.exchange-rate}")
    private String filePath;

    public BigDecimal getUsdExchangeRateFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Double> exchangeRate;
        try {
            exchangeRate = mapper.readValue(new File(filePath), Map.class);
        } catch (IOException exception) {
            log.error("Error reading file");
            throw new ApplicationException(ErrorType.INVALID_EXCHANGE_RATE_FILE);
        }
        log.info("Successfully exchange rate from file");
        return BigDecimal.valueOf(exchangeRate.get("USD"));
    }

    @Cacheable(value = "exchangeCache", key = "'usd'")
    public BigDecimal getUsdFromService() {
        try {
            log.info("Successfully fetching USD exchange rate from service");
            return restTemplate.getForObject("http://localhost:8081/api/v2/exchange/usd",
                    BigDecimal.class);
        } catch (RestClientException e) {
            log.warn("Failed to fetch USD exchange rate from service.");
            return getUsdExchangeRateFromFile();
        }
    }
}
