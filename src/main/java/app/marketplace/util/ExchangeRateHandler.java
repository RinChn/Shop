package app.marketplace.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import app.marketplace.exception.ApplicationException;
import app.marketplace.exception.ErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class ExchangeRateHandler {

    @Value("${app.file-names.exchange-rate}")
    private String filePath;

    public BigDecimal getUsdExchangeRate() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Double> exchangeRate;
        try {
            exchangeRate = mapper.readValue(new File(filePath), Map.class);
        } catch (IOException exception) {
            throw new ApplicationException(ErrorType.INVALID_EXCHANGE_RATE_FILE);
        }
        return BigDecimal.valueOf(exchangeRate.get("USD"));
    }
}
