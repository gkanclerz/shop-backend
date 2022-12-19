package pl.nullpointerexception.shop.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nullpointerexception.shop.order.model.dto.InitOrder;
import pl.nullpointerexception.shop.order.model.dto.OrderDto;
import pl.nullpointerexception.shop.order.model.dto.OrderSummary;
import pl.nullpointerexception.shop.order.service.OrderService;
import pl.nullpointerexception.shop.order.service.ShipmentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ShipmentService shipmentService;

    @PostMapping
    public OrderSummary placeOrder(@RequestBody OrderDto orderDto){
        return orderService.placeOrder(orderDto);
    }

    @GetMapping("/initData")
    public InitOrder initData(){
        return InitOrder.builder()
                .shipments(shipmentService.getShipments())
                .build();
    }
}
