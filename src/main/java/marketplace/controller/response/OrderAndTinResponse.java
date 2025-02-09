package marketplace.controller.response;

import lombok.*;
import marketplace.util.OrderStatus;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderAndTinResponse {
    Integer number;
    String emailCustomer;
    String tinCustomer;
    BigDecimal price;
    OrderStatus status;
}
