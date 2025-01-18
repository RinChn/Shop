package app.marketplace.dto;

import lombok.Setter;
import app.marketplace.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductResponse {
    private Integer article;
    private String name;
    private String description;
    private Category categories;
    private BigDecimal price;
    private Integer quantity;
    private Timestamp dateOfLastChangesQuantity;
    private Timestamp dateOfCreation;
    private Boolean isAvailable;
}
