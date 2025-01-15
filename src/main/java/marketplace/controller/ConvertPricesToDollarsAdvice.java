package marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import marketplace.dto.ProductResponse;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ConvertPricesToDollarsAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return ProductResponse.class.isAssignableFrom(returnType.getParameterType()) ||
                (List.class.isAssignableFrom(returnType.getParameterType()) &&
                        returnType.getGenericParameterType().getTypeName().contains("ProductResponse"));
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        String fileName = "src/main/resources/parameters/ExchangeRate.json";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Double> exchangeRate;
        try {
            exchangeRate = mapper.readValue(new File(fileName), Map.class);
        } catch (IOException exception) {
            throw new ApplicationException(ErrorType.INVALID_EXCHANGE_RATE_FILE);
        }
        if (body instanceof ProductResponse product) {
            product.setPrice(product.getPrice().multiply(BigDecimal.valueOf(exchangeRate.get("USD"))));
            return product;
        } else if (body instanceof List) {
            List<ProductResponse> products = (List<ProductResponse>) body;
            for (ProductResponse product : products) {
                product.setPrice(product.getPrice().multiply(BigDecimal.valueOf(exchangeRate.get("USD"))));
            }
            return products;
        }
        return body;
    }
}
