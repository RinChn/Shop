package app.marketplace.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import app.marketplace.entity.Category;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestCreate {
    @NotNull(message = "The article cannot be empty")
    @Positive(message = "The article cannot be negative or zero")
    private Integer article;
    @NotBlank(message = "The name cannot be empty")
    private String name;
    private String description;
    private Category categories;
    @NotNull(message = "The price cannot be empty")
    @PositiveOrZero(message = "The price cannot be negative")
    private BigDecimal price;
    @PositiveOrZero(message = "The quantity cannot be negative")
    @Builder.Default
    private Integer quantity = 0;
    @Builder.Default
    private Boolean isAvailable = true;
}
