package pl.nullpointerexception.shop.common.model;

public enum OrderStatus {
    NEW("Nowe"),
    PAID("Opłacone"),
    PROCESSING("Przetwarzanie"),
    WAITING_FOR_DELIVERY("Czeka na dostawę"),
    COMPLETED("Zrealizowane"),
    CANCELED("Anulowane"),
    REFUND("Zwrócone");

    private String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
