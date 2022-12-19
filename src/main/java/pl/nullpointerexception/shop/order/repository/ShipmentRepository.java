package pl.nullpointerexception.shop.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.nullpointerexception.shop.order.model.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
}
