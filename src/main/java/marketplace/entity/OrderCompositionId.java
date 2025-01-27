package marketplace.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@Setter
@Getter
public class OrderCompositionId implements Serializable {
    @Column(name = "order_id")
    UUID orderId;
    @Column(name = "product_id")
    UUID productId;
}
