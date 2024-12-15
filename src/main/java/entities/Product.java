package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
            @Column(name = "id", nullable = false)
    UUID id;
    @Column(name = "name", nullable = false, length = 255)
            @Setter
    String name;
    @Column(name = "description")
            @Setter
    String description;
    @Column(name = "categories")
            @Setter
    Category categories;
    @Column(name = "price", nullable = false)
            @Setter
    float price;
    @Column(name = "quantity")
            @Setter
    int quantity = 0;
    @Column(name = "article_number", nullable = false)
    long articleNumber;
    @Column(name = "date_last_changes_quantity")
            @Setter
    Timestamp dateOfLastChangesQuantity = new Timestamp(System.currentTimeMillis());
    @Column(name = "date_creation")
    final Timestamp dateOfCreation = new Timestamp(System.currentTimeMillis());
}
