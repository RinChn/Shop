package marketplace.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateHandler {

    @Value("${app.file-names.exchange-rate}")
    private String filePath;
    private final RateFeignClient rateFeignClient;


    @Cacheable(value = "exchangeCache", key = "#currencyName")
    public BigDecimal getRateFromService(String currencyName) {
        try {
            log.info("Successfully fetching {} exchange rate from service", currencyName);
            return rateFeignClient.getExchangeRate(currencyName.toLowerCase());
        } catch (RestClientException e) {
            log.warn("Failed to fetch {} exchange rate from service.", currencyName);
            return getExchangeRateFromFile(currencyName.toUpperCase());
        }
    }

    public BigDecimal getExchangeRateFromFile(String currencyName) {
        Map<String, Double> exchangeRate;
        try {
            exchangeRate = new ObjectMapper().readValue(new File(filePath), Map.class);
        } catch (IOException exception) {
            log.error("Error reading file");
            throw new ApplicationException(ErrorType.INVALID_EXCHANGE_RATE_FILE);
        }
        log.info("Successfully exchange rate from file");
        return BigDecimal.valueOf(exchangeRate.get(currencyName));
    }
}
