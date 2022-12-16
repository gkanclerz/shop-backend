package pl.nullpointerexception.shop.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.nullpointerexception.shop.cart.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Long countByCartId(Long cartId);
}
