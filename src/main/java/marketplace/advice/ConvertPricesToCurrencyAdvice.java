package marketplace.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import marketplace.util.ExchangeRateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.dto.ProductResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ConvertPricesToCurrencyAdvice implements ResponseBodyAdvice<Object> {

    private final ExchangeRateHandler exchangeRateHandler;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return "getProductByArticle".equals(Objects.requireNonNull(returnType.getMethod()).getName());
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String currencyName = httpServletRequest.getHeader("Currency");
        HttpSession session = httpServletRequest.getSession();
        String currentCurrency = exchangeRateHandler.getCurrentCurrency(currencyName, session);
        log.info("Convert prices to {}", currentCurrency);
        ProductResponse product = (ProductResponse) body;
        BigDecimal exchangeRate = BigDecimal.valueOf(1.0);
        if (currentCurrency.equals("USD")) {
            exchangeRate = exchangeRateHandler.getUsdFromService();
        } else if (currentCurrency.equals("EUR")) {
            exchangeRate = exchangeRateHandler.getEurFromService();
        }
        product.setPrice(product.getPrice()
                .divide(exchangeRate, 2, RoundingMode.HALF_UP));
        return product;
    }
}
