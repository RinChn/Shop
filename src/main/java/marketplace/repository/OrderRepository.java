package marketplace.repository;

import marketplace.entity.Order;
import marketplace.entity.User;
import marketplace.util.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT MAX(o.number) AS max_number FROM Order o WHERE o.customer = :customer")
    Long getLastNumberOfUserOrders(@Param("customer") User customer);

    @Query("SELECT o FROM Order o WHERE o.customer = :customer AND o.status = :status")
    Optional<Order> findOrderOfCustomerByStatus(@Param("customer") User customer, @Param("status") OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.number = :number AND o.customer = :customer")
    Optional<Order> findOrderByNumber(@Param("number") Integer number, @Param("customer") User customer);

    @Query("SELECT o FROM Order o WHERE o.customer = :customer")
    List<Order> findAllOrdersOfCustomer(@Param("customer") User customer);

    @Query("SELECT o FROM Order o JOIN FETCH o.customer")
    List<Order> findAllFetch();
}
