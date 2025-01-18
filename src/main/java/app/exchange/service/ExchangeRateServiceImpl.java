package app.exchange.service;

import app.exception.ApplicationException;
import app.exception.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Value("${app.file-names.exchange-rate}")
    private String filePath;

    @Override
    public BigDecimal getUsdExchangeRate() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Double> exchangeRate;
        try {
            exchangeRate = mapper.readValue(new File(filePath), Map.class);
        } catch (IOException exception) {
            log.error("Error reading file");
            throw new ApplicationException(ErrorType.INVALID_EXCHANGE_RATE_FILE);
        }
        log.info("Successful sending of the dollar exchange rate: {}", exchangeRate.get("USD"));
        return BigDecimal.valueOf(exchangeRate.get("USD"));
    }
}
