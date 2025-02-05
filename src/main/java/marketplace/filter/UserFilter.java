package marketplace.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.entity.User;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import marketplace.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String currencyMail = request.getHeader("Login");
        if (currencyMail != null && !currencyMail.isEmpty()) {
            User currentUser = userRepository.findByEmail(currencyMail)
                    .orElseThrow(() -> new ApplicationException(ErrorType.UNREGISTERED_MAIL));
            log.info("Update current user: {}", currentUser.getEmail());
            session.setAttribute("user", currentUser);
        }
        filterChain.doFilter(request, response);
    }
}
