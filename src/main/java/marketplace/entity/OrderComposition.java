package marketplace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_compositions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderComposition {
    @EmbeddedId
    OrderCompositionId id;
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    Order order;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

}
