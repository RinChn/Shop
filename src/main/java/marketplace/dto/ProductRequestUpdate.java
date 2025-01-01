package marketplace.dto;

import jakarta.validation.constraints.PositiveOrZero;
import marketplace.entity.Category;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ProductRequestUpdate {
    private String name;
    private String description;
    private Category categories;
    @PositiveOrZero(message = "The price cannot be negative")
    private BigDecimal price;
    @PositiveOrZero(message = "The quantity cannot be negative")
    private Integer quantity;
}
