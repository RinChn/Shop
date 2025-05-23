package marketplace.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.entity.User;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserHandler {

    final HttpServletRequest request;

    @Cacheable(value = "userLoginCache", key = "'email'")
    public User getCurrentUser() {
        log.info("getCurrentUser");
        User customer = (User) request.getSession().getAttribute("user");
        if (customer == null)
            throw new ApplicationException(ErrorType.UNIDENTIFIED_USER);
        return customer;
    }
}
