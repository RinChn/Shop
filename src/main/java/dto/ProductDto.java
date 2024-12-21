package dto;

import entities.Category;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProductDto {
    UUID id;
    String name;
    String description;
    Category categories;
    Float price;
    Integer quantity = 0;
    Long articleNumber;
    Timestamp dateOfLastChangesQuantity = new Timestamp(System.currentTimeMillis());

}
