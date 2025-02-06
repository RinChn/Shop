package marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import marketplace.util.Category;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DetailedPartOfOrderDto {
    Integer article;
    String name;
    String description;
    Category category;
    BigDecimal totalPriceForOrder;
    Integer quantityInOrder;
}
