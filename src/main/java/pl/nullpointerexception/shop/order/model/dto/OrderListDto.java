package pl.nullpointerexception.shop.order.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.nullpointerexception.shop.common.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderListDto {
    private Long id;
    private LocalDateTime placeDate;
    private String orderStatus;
    private BigDecimal grossValue;
}
