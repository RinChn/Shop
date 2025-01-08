package marketplace.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ValueGenerationType;

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
    Integer quantity;
    @Column(name = "date_last_changes_quantity", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    Timestamp dateOfLastChangesQuantity;
    @Column(name = "date_creation", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    Timestamp dateOfCreation;
    @Column(name = "is_available", nullable = false, columnDefinition = "True")
    Boolean isAvailable;

    @PrePersist
    public void prePersist() {
        if (this.quantity == null) {
            this.quantity = 0;
        }
        if (this.dateOfLastChangesQuantity == null) {
            this.dateOfLastChangesQuantity = new Timestamp(System.currentTimeMillis());
        }
        if (this.dateOfCreation == null) {
            this.dateOfCreation = new Timestamp(System.currentTimeMillis());
            this.dateOfCreation = new Timestamp(System.currentTimeMillis());
        }
        if (this.isAvailable == null) {
            this.isAvailable = true;
        }
    }

}
