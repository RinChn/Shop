package dto;

import entity.Category;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private String categories;
    private Float price;
    private Category categories;
    private BigDecimal price;
    private Integer quantity;
}
