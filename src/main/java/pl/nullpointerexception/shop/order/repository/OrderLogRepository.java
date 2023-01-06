package pl.nullpointerexception.shop.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.nullpointerexception.shop.order.model.OrderLog;

public interface OrderLogRepository extends JpaRepository<OrderLog, Long> {
}
