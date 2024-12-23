package dto;

import entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class ProductResponse {
    private Integer article;
    private String name;
    private String description;
    private Category categories;
    private Float price;
    private Integer quantity;
    private Timestamp dateOfLastChangesQuantity;
    private Timestamp dateOfCreation;
}
