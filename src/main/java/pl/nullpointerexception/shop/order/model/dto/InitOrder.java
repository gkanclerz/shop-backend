package pl.nullpointerexception.shop.order.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.nullpointerexception.shop.order.model.Shipment;

import java.util.List;

@Getter
@Builder
public class InitOrder {
    private List<Shipment> shipments;
}
