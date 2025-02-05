package marketplace.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderCompositionResponse {
    Integer productArticle;
    Integer productQuantity;
    BigDecimal productPrice;
}
