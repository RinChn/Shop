package dto;

import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private String categories;
    private Float price;
    private Integer quantity;
    private Timestamp dateOfLastChangesQuantity;
    private Timestamp dateOfCreation;
}
