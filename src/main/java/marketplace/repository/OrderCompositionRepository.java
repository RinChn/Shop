package marketplace.repository;

import marketplace.entity.Order;
import marketplace.entity.OrderComposition;
import marketplace.entity.OrderCompositionId;
import marketplace.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCompositionRepository  extends JpaRepository<OrderComposition, OrderCompositionId> {

    @Query("SELECT oc FROM OrderComposition oc JOIN FETCH oc.product WHERE oc.order = :order")
    List<OrderComposition> findCompositionsOfOrder(@Param("order") Order order);

    @Query("SELECT oc FROM OrderComposition oc JOIN FETCH oc.order WHERE oc.product = :product")
    List<OrderComposition> findCompositionsOfProduct(@Param("product") Product product);
}
