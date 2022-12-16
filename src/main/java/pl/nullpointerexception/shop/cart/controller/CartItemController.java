package pl.nullpointerexception.shop.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.nullpointerexception.shop.cart.service.CartItemService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;

    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Long id){
        cartItemService.delete(id);
    }

    @GetMapping("/count/{cartId}")
    public Long countItemInCart(@PathVariable Long cartId){
        return cartItemService.countItemInCart(cartId);
    }

}
