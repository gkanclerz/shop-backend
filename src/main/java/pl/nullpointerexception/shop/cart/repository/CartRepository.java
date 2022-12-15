package pl.nullpointerexception.shop.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.nullpointerexception.shop.cart.model.Cart;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
