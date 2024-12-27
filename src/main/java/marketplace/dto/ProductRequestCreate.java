package marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import marketplace.entity.Category;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ProductRequestCreate {
    @NotNull(message = "The name cannot be empty")
    private String name;
    private String description;
    private Category categories;
    @NotNull(message = "The name cannot be empty")
    @PositiveOrZero(message = "The price cannot be negative")
    private BigDecimal price;
    @PositiveOrZero(message = "The quantity cannot be negative")
    private Integer quantity;
}
