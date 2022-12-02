package pl.nullpointerexception.shop.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.nullpointerexception.shop.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
