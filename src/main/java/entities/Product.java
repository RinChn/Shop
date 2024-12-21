package entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "product")
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
    Timestamp dateOfLastChangesQuantity;
    @Column(name = "date_creation")
    Timestamp dateOfCreation;
}
