package marketplace.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.entity.User;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserHandler {

    private final RestTemplate restTemplate = new RestTemplate();

    final HttpServletRequest request;
    @Value("${app.tax.url-get-tin}")
    private String tinUrl;
    @Value("${app.tax.url}")
    private String taxServiceUrl;

    @Cacheable(value = "userLoginCache", key = "'email'")
    public User getCurrentUser() {
        log.info("getCurrentUser");
        User customer = (User) request.getSession().getAttribute("user");
        if (customer == null)
            throw new ApplicationException(ErrorType.UNIDENTIFIED_USER);
        return customer;
    }

    public List<String> getTinFromService(List<String> emails) {
        try {
            log.info("Successfully fetching TIN exchange rate from service");
            String url = taxServiceUrl + tinUrl;
            log.info(url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<String>> requestEntity = new HttpEntity<>(emails, headers);
            ResponseEntity<List> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    List.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            log.warn("Failed to fetch TIN exchange rate from service.");
            return List.of();
        }
    }
}
