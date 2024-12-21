package entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Builder
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
            @Column(name = "articleNumber", nullable = false)
    UUID articleNumber;
    @Column(name = "name", nullable = false, length = 255)
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "categories")
    Category categories;
    @Column(name = "price", nullable = false)
    Float price;
    @Column(name = "quantity")
    Integer quantity;
    @Column(name = "date_last_changes_quantity")
    Timestamp dateOfLastChangesQuantity = new Timestamp(System.currentTimeMillis());
    @Column(name = "date_creation")
    Timestamp dateOfCreation = new Timestamp(System.currentTimeMillis());

    public Product() {

    }
}
