package marketplace.repository;

import marketplace.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID>  {

    @Query("SELECT d from Document d WHERE d.name = :fileName")
    public Optional<Document> findByName(String fileName);

}
