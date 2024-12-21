package dto;

import entities.Category;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProductRequest {
    String name;
    String description;
    Category categories;
    Float price;
    Integer quantity = 0;
    Timestamp dateOfLastChangesQuantity = new Timestamp(System.currentTimeMillis());
    Timestamp dateOfCreation = new Timestamp(System.currentTimeMillis());
}
