package marketplace.repository;

import marketplace.entity.Category;
import marketplace.entity.Product;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p WHERE p.article = :article")
    Optional<Product> findByArticle(@Param("article") Integer article);

    @Query("SELECT p FROM Product p WHERE p.name = :name AND p.description = :description AND p.categories = :category")
    Optional<Product> findByNameAndDescriptionAndCategories(@Param("name") String name, @Param("description") String description,
                                                         @Param("category") Category category);
}
