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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ExchangeRateHandler {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.file-names.exchange-rate}")
    private String filePath;
    @Value("${app.exchange.url}")
    private String exchangeServiceUrl;
    @Value("${app.exchange.url-get-currency}")
    private String currencyUrl;

    public String getAndUpdateCurrentCurrency(String currencyName, HttpSession session) {
        if (currencyName != null && !currencyName.isEmpty()) {
            currencyName = currencyName.toUpperCase();
            List<String> allCurrencies = Arrays.stream(CurrencyNames.values())
                    .map(Enum::toString).toList();
            for (String key : allCurrencies) {
                if (currencyName.equals(key)) {
                    session.setAttribute("currency", currencyName);
                    return currencyName;
                }
            }
        }
        if (session.getAttribute("currency") != null)
            return session.getAttribute("currency").toString();
        else {
            session.setAttribute("currency", CurrencyNames.RUB.toString());
            return CurrencyNames.RUB.toString();
        }
    }

    @Cacheable(value = "exchangeCache", key = "'usd'")
    public BigDecimal getUsdFromService() {
        try {
            log.info("Successfully fetching USD exchange rate from service");
            return restTemplate.getForObject(exchangeServiceUrl  + currencyUrl + "/usd",
                    BigDecimal.class);
        } catch (RestClientException e) {
            log.warn("Failed to fetch USD exchange rate from service.");
            return getExchangeRateFromFile(CurrencyNames.USD.toString());
        }
    }

    @Cacheable(value = "exchangeCache", key = "'eur'")
    public BigDecimal getEurFromService() {
        try {
            log.info("Successfully fetching EUR exchange rate from service");
            return restTemplate.getForObject(exchangeServiceUrl + currencyUrl + "/eur",
                    BigDecimal.class);
        } catch (RestClientException e) {
            log.warn("Failed to fetch EUR exchange rate from service.");
            return getExchangeRateFromFile(CurrencyNames.EUR.toString());
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
