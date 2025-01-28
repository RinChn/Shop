package marketplace.repository;

import marketplace.entity.Order;
import marketplace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT COUNT(*) AS order_count FROM Order o WHERE o.customer = :customer")
    Long getNumberOfUserOrders(@Param("customer") User customer);
}
