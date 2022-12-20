package pl.nullpointerexception.shop.order.service.mapper;

import pl.nullpointerexception.shop.order.model.Order;

import java.time.format.DateTimeFormatter;

public class OrderEmailMessageMapper {

    public static String createEmailMessage(Order order) {
        return "Twoje zamówienie o id: " + order.getId() +
                "\n Data złożenia: " + order.getPlaceDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                "\n Wartość: " + order.getGrossValue() + " PLN " +
                "\n\n" +
                "\n Płatność: " + order.getPayment().getName() +
                (order.getPayment().getNote() != null ? "\n" + order.getPayment().getNote() : "") +
                "\n\n Dziękujemy za zakupy.";
    }

}
