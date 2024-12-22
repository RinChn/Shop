package app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
            @Column(name = "article_number", nullable = false)
    UUID articleNumber;
    @Column(name = "name", nullable = false, length = 255)
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "categories")
    Category categories;
    @Column(name = "price", nullable = false)
    Float price;
    @Column(name = "quantity", columnDefinition = "integer default 0")
    Integer quantity;
    @Column(name = "date_last_changes_quantity", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    Timestamp dateOfLastChangesQuantity;
    @Column(name = "date_creation", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    Timestamp dateOfCreation;

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
    }

}
