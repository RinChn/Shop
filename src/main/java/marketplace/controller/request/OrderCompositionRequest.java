package marketplace.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCompositionRequest {
    @NotNull(message = "The article cannot be empty")
    private Integer productArticle;
    @Positive(message = "The quantity cannot be negative or zero")
    private Integer productQuantity;
}
