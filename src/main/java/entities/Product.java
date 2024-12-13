package entities;

import java.util.ArrayList;
import java.sql.Timestamp;
import jakarta.persistence.*;

public class Product {
    String name;
    String description;
    String categories;
    float price;
    int quantity;
    long articleNumber;
    Timestamp dateOfLastChangesQuantity;
    Timestamp dateOfCreation;
}
