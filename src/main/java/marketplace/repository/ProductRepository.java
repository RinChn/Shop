package marketplace.repository;

import jakarta.persistence.LockModeType;
import marketplace.entity.Product;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT p FROM Product p")
    List<Product> getAllProductsForLock();

    @Query("SELECT p FROM Product p WHERE p.article = :article")
    Optional<Product> findByArticle(@Param("article") Integer article);

    @Query("SELECT p FROM Product p WHERE (:name IS NULL OR p.name LIKE %:name%) " +
            "AND (:quantity IS NULL OR p.quantity >= :quantity) AND (:price IS NULL OR p.price <= :price) " +
            "AND (:isAvailable IS NULL OR p.isAvailable = :isAvailable)")
    List<Product> searchUsingFilter(@Param("name") String name, @Param("quantity") Integer quantity,
                                    @Param("price") BigDecimal price, @Param("isAvailable") Boolean isAvailable);
}
