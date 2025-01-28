package marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestCreate {
    @NotBlank(message = "The email cannot be empty")
    String emailCustomer;
}
