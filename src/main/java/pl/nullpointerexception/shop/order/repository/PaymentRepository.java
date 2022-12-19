package pl.nullpointerexception.shop.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.nullpointerexception.shop.order.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
