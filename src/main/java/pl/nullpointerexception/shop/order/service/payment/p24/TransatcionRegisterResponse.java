package pl.nullpointerexception.shop.order.service.payment.p24;

import lombok.Getter;

@Getter
public class TransatcionRegisterResponse {
    private Data data;
}

record Data(String token){}