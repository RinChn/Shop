package marketplace.advice;

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
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.math.RoundingMode;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ConvertPricesToDollarsAdvice implements ResponseBodyAdvice<Object> {

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
        log.info("Convert prices to dollars");
        ProductResponse product = (ProductResponse) body;
        product.setPrice(product.getPrice()
                .multiply(exchangeRateHandler.getUsdFromService()));
        return product;
    }
}
