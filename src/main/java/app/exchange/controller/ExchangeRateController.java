package app.exchange.controller;

import app.exchange.service.ExchangeRateServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/v1/exchange")
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateServiceImpl exchangeRateService;

    @GetMapping("/usd")
    public BigDecimal getExchangeRateUSD() {
        log.info("Request to receive the dollar exchange rate");
        return exchangeRateService.getUsdExchangeRate();
    }
}
