package marketplace.dto;

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
    private BigDecimal price;
    private Integer quantity;
}
