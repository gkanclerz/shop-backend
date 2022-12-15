package pl.nullpointerexception.shop.cart.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SummaryDto {
    private BigDecimal grossValue;
}
