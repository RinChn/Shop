package marketplace.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    UUID id;
    @Column(name = "article", nullable = false, unique = true)
    Integer article;
    @Column(name = "name", nullable = false, length = 255)
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "categories")
    @Enumerated(EnumType.STRING)
    Category categories;
    @Column(name = "price", nullable = false)
    BigDecimal price;
    @Column(name = "quantity", columnDefinition = "integer default 0")
    @Builder.Default
    Integer quantity = 0;
    @Column(name = "date_last_changes_quantity", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    @Builder.Default
    @UpdateTimestamp
    Timestamp dateOfLastChangesQuantity = new Timestamp(System.currentTimeMillis());
    @Column(name = "date_creation", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    @Builder.Default
    Timestamp dateOfCreation = new Timestamp(System.currentTimeMillis());
    @Column(name = "is_available", nullable = false, columnDefinition = "True")
    @Builder.Default
    Boolean isAvailable = true;
}
