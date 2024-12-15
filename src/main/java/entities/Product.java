package entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
            @Column(name = "id", nullable = false)
    UUID id;
    @Column(name = "name", nullable = false, length = 255)
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "categories")
    Category categories;
    @Column(name = "price", nullable = false)
    float price;
    @Column(name = "quantity")
    int quantity = 0;
    @Column(name = "article_number", nullable = false)
    long articleNumber;
    @Column(name = "date_last_changes_quantity")
    Timestamp dateOfLastChangesQuantity = new Timestamp(System.currentTimeMillis());
    @Column(name = "date_creation")
    Timestamp dateOfCreation = new Timestamp(System.currentTimeMillis());
}
