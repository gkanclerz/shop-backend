package pl.nullpointerexception.shop.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.nullpointerexception.shop.order.model.OrderRow;

public interface OrderRowRepository extends JpaRepository<OrderRow,Long> {
}
