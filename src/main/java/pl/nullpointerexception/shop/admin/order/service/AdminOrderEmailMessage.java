package pl.nullpointerexception.shop.admin.order.service;

import pl.nullpointerexception.shop.admin.order.model.AdminOrderStatus;

public class AdminOrderEmailMessage {

    public static String createProcessingEmailMessage(Long id, AdminOrderStatus newStatus) {
        return "Twoje zamówienie: " + id + " jest przetwarzane." +
                "\n Status został zmieniony na: " + newStatus.getValue() +
                "\n Twoje zamówienie jest przetwarzane przez naszych pracowników" +
                "\n Po skompletowaniu niezwłocznie przekażemy je do wysyłki" +
                "\n Pozdrawiamy" +
                "\n Sklep Shop";
    }

    public static String createCompletedEmailMessage(Long id, AdminOrderStatus newStatus){
        return "Twoje zamówienie: " + id + " zostało zrealizowane." +
                "\n Status twojego zamówienia został zmieniony na: " + newStatus.getValue() +
                "\n Dziekujęmy za zakupy i zapraszamy ponownie!" +
                "\n Sklep shop";
    }

    public static String createRefundEmailMessage(Long id, AdminOrderStatus newStatus){
        return "Twoje zamówienie: " + id + " zostało zwrócone." +
                "\n Status twojego zamówienia został zmieniony na: " + newStatus.getValue() +
                "\n Pozdrawiamy" +
                "\n Sklep Shop";
    }
}
