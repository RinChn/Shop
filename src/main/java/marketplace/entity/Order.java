package marketplace.entity;

import jakarta.persistence.*;
import lombok.*;
import marketplace.util.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    UUID id;
    @Column(name = "number", nullable = false)
    Integer number;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    User customer;
    @Column(name = "price", columnDefinition = "integer default 0")
            @Builder.Default
    BigDecimal price = BigDecimal.ZERO;
    @Column(name = "status", columnDefinition = "varchar(255) default 'CREATED'")
            @Builder.Default
    OrderStatus status = OrderStatus.CREATED;
}
