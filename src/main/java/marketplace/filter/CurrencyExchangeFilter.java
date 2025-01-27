package marketplace.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import marketplace.util.CurrencyNames;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CurrencyExchangeFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Current currency exchange filter is enabled");
        HttpSession session = request.getSession();
        String currencyName = request.getHeader("Currency");
        if (currencyName != null && !currencyName.isEmpty()) {
            currencyName = currencyName.toUpperCase();
            Set<String> allCurrencies = Arrays.stream(CurrencyNames.values())
                    .map(Enum::toString)
                    .collect(Collectors.toSet());
            if (allCurrencies.contains(currencyName)) {
                session.setAttribute("currency", currencyName);
            }
        }
        if (session.getAttribute("currency") == null)
            session.setAttribute("currency", CurrencyNames.RUB.toString());
        log.info("Current currency is {}", session.getAttribute("currency"));
        filterChain.doFilter(request, response);
    }
}
