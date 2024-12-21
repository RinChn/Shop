package dto;

import entities.Category;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private Category categories;
    private Float price;
    private Integer quantity = 0;
    private Timestamp dateOfLastChangesQuantity = new Timestamp(System.currentTimeMillis());
    private Timestamp dateOfCreation = new Timestamp(System.currentTimeMillis());
}
