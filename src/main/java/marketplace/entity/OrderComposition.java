package marketplace.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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
    @Column(name = "product_quantity", columnDefinition = "integer default 1")
            @Builder.Default
    Integer productQuantity = 1;
    @Column(name = "price")
    BigDecimal price;
}
