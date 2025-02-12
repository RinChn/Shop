package marketplace.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "RateClient", url = "${app.currency.url}" + "${app.currency.url-get-currency}")
public interface RateFeignClient {
    @GetMapping("/{currency}")
    BigDecimal getExchangeRate(@PathVariable("currency") String currency);
}
