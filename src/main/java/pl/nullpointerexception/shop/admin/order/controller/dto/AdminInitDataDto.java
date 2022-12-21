package pl.nullpointerexception.shop.admin.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class AdminInitDataDto {
    private Map<String, String> orderStatuses;
}
