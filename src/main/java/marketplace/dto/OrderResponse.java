package marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import marketplace.util.OrderStatus;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderResponse {
    Integer number;
    String email_customer;
    BigDecimal price;
    OrderStatus status;
}
