package marketplace.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
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
    @Value("${app.exchange.url}")
    private String exchangeServiceUrl;

    public String getCurrentCurrency(String currencyName, HttpSession session) {
        if (currencyName != null && !currencyName.isEmpty()) {
            currencyName = currencyName.toUpperCase();
            Map<String, Double> allCurrencies = readAllCurrenciesFromFile();
            for (String key : allCurrencies.keySet()) {
                if (currencyName.equals(key)) {
                    session.setAttribute("currency", currencyName);
                    return currencyName;
                }
            }
        }
        if (session.getAttribute("currency") != null)
            return session.getAttribute("currency").toString();
        else {
            session.setAttribute("currency", "RUB");
            return "RUB";
        }
    }

    @Cacheable(value = "exchangeCache", key = "'usd'")
    public BigDecimal getUsdFromService() {
        try {
            log.info("Successfully fetching USD exchange rate from service");
            return restTemplate.getForObject(exchangeServiceUrl + "/api/v2/exchange/usd",
                    BigDecimal.class);
        } catch (RestClientException e) {
            log.warn("Failed to fetch USD exchange rate from service.");
            return getExchangeRateFromFile("USD");
        }
    }

    @Cacheable(value = "exchangeCache", key = "'eur'")
    public BigDecimal getEurFromService() {
        try {
            log.info("Successfully fetching EUR exchange rate from service");
            return restTemplate.getForObject(exchangeServiceUrl + "/api/v2/exchange/eur",
                    BigDecimal.class);
        } catch (RestClientException e) {
            log.warn("Failed to fetch EUR exchange rate from service.");
            return getExchangeRateFromFile("EUR");
        }
    }

    public BigDecimal getExchangeRateFromFile(String currencyName) {
        Map<String, Double> exchangeRate = readAllCurrenciesFromFile();
        log.info("Successfully exchange rate from file");
        return BigDecimal.valueOf(exchangeRate.get(currencyName));
    }

    private Map<String, Double> readAllCurrenciesFromFile() {
        try {
            return new ObjectMapper().readValue(new File(filePath), Map.class);
        } catch (IOException exception) {
            log.error("Error reading file");
            throw new ApplicationException(ErrorType.INVALID_EXCHANGE_RATE_FILE);
        }
    }
}
