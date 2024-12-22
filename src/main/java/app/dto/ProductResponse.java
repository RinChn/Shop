package app.dto;

import app.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ProductResponse {
    private UUID articleNumber;
    private String name;
    private String description;
    private Category categories;
    private Float price;
    private Integer quantity;
    private Timestamp dateOfLastChangesQuantity;
    private Timestamp dateOfCreation;
}
