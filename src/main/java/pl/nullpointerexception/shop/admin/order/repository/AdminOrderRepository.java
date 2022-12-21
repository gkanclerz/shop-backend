package pl.nullpointerexception.shop.admin.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.nullpointerexception.shop.admin.order.model.AdminOrder;

public interface AdminOrderRepository extends JpaRepository<AdminOrder, Long> {
}
