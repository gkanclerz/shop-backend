package pl.nullpointerexception.shop.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nullpointerexception.shop.common.mail.EmailClientService;
import pl.nullpointerexception.shop.common.model.Cart;

import pl.nullpointerexception.shop.common.repository.CartItemRepository;
import pl.nullpointerexception.shop.common.repository.CartRepository;
import pl.nullpointerexception.shop.order.model.Order;
import pl.nullpointerexception.shop.order.model.Payment;
import pl.nullpointerexception.shop.order.model.Shipment;
import pl.nullpointerexception.shop.order.model.dto.OrderDto;
import pl.nullpointerexception.shop.order.model.dto.OrderSummary;
import pl.nullpointerexception.shop.order.repository.OrderRepository;
import pl.nullpointerexception.shop.order.repository.OrderRowRepository;
import pl.nullpointerexception.shop.order.repository.PaymentRepository;
import pl.nullpointerexception.shop.order.repository.ShipmentRepository;

import static pl.nullpointerexception.shop.order.service.mapper.OrderEmailMessageMapper.createEmailMessage;
import static pl.nullpointerexception.shop.order.service.mapper.OrderMapper.createNewOrder;
import static pl.nullpointerexception.shop.order.service.mapper.OrderMapper.createOrderSummary;
import static pl.nullpointerexception.shop.order.service.mapper.OrderMapper.mapToOrderRow;
import static pl.nullpointerexception.shop.order.service.mapper.OrderMapper.mapToOrderRowWithQuantity;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRowRepository orderRowRepository;
    private final ShipmentRepository shipmentRepository;
    private final PaymentRepository paymentRepository;
    private final EmailClientService emailClientService;

    @Transactional
    public OrderSummary placeOrder(OrderDto orderDto, Long userId) {
        Cart cart = cartRepository.findById(orderDto.getCartId()).orElseThrow();
        Shipment shipment = shipmentRepository.findById(orderDto.getShipmentId()).orElseThrow();
        Payment payment = paymentRepository.findById(orderDto.getPaymentId()).orElseThrow();
        Order newOrder = orderRepository.save(createNewOrder(orderDto, cart, shipment, payment, userId));
        saveOrderRows(cart, newOrder.getId(),shipment);
        clearOrderCart(orderDto);
        sendConfimEmail(newOrder);
        return createOrderSummary(payment, newOrder);
    }

    private void sendConfimEmail(Order newOrder) {
        emailClientService.getInstance()
                .send(newOrder.getEmail(),
                        "Twoje zamówienie zostało przyjęte",
                        createEmailMessage(newOrder));
    }

    private void clearOrderCart(OrderDto orderDto) {
        cartItemRepository.deleteByCartId(orderDto.getCartId());
        cartRepository.deleteCartById(orderDto.getCartId());
    }

    private void saveOrderRows(Cart cart, Long orderId, Shipment shipment) {
        saveProductRows(cart, orderId);
        saveShipmentRow(orderId, shipment);
    }

    private void saveShipmentRow(Long orderId, Shipment shipment) {
        orderRowRepository.save(mapToOrderRow(orderId, shipment));
    }

    private void saveProductRows(Cart cart, Long orderId) {
        cart.getItems().stream()
                .map(cartItem -> mapToOrderRowWithQuantity(orderId, cartItem))
                .peek(orderRowRepository::save)
                .toList();
    }

}
