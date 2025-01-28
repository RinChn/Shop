package marketplace.dto;

import lombok.*;
import marketplace.util.OrderStatus;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestUpdate {
    OrderStatus status;
}
