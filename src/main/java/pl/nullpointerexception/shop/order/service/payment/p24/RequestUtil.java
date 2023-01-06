package pl.nullpointerexception.shop.order.service.payment.p24;

import org.apache.commons.codec.digest.DigestUtils;
import pl.nullpointerexception.shop.order.model.Order;
import pl.nullpointerexception.shop.order.model.dto.NotificationReceiveDto;

import java.math.BigDecimal;

public class RequestUtil {


    public static TransactionRegisterRequest createRegisterRequest(PaymentMethodP24Config config, Order newOrder){
        return TransactionRegisterRequest.builder()
                .merchantId(config.getMerchantId())
                .posId(config.getPosId())
                .sessionId(createSessionId(newOrder))
                .amount(newOrder.getGrossValue().movePointRight(2).intValue())
                .currency("PLN")
                .description("Zamówienie id: " + newOrder.getId())
                .email(newOrder.getEmail())
                .client(newOrder.getFirstname() + " " + newOrder.getLastname())
                .country("PL")
                .language("pl")
                .urlReturn(getUrlReturn(newOrder.getOrderHash(),config))
                .urlStatus(generateStatusUrl(newOrder.getOrderHash(),config))
                .sign(createSign(newOrder,config))
                .encoding("UTF-8")
                .build();
    }

    public static TransactionVerifyRequest createVerifyRequest(PaymentMethodP24Config config, Order order, NotificationReceiveDto receiveDto){
        return TransactionVerifyRequest.builder()
                .merchantId(config.getMerchantId())
                .posId(config.getPosId())
                .sessionId(createSessionId(order))
                .amount(order.getGrossValue().movePointRight(2).intValue())
                .currency("PLN")
                .orderId(receiveDto.getOrderId())
                .sign(createVerifySign(receiveDto, order,config))
                .build();
    }

    private static String generateStatusUrl(String orderHash, PaymentMethodP24Config config) {
        String baseUrl = config.isTestMode() ? config.getTestUrlStatus() : config.getUrlStatus();
        return baseUrl + "/orders/notification/" + orderHash;
    }

    private static String getUrlReturn(String orderHash, PaymentMethodP24Config config) {
        String baseUrl = config.isTestMode() ? config.getTestUrlReturn() : config.getUrlReturn();
        return baseUrl + "/order/notification/" + orderHash;
    }

    private static String createSign(Order newOrder, PaymentMethodP24Config config) {
        String json = "{\"sessionId\":\"" + createSessionId(newOrder) +
                "\",\"merchantId\":" + config.getMerchantId() +
                ",\"amount\":" + newOrder.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\",\"crc\":\""+ (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }

    private static String createVerifySign(NotificationReceiveDto receiveDto, Order order, PaymentMethodP24Config config) {
        String json = "{\"sessionId\":\"" + createSessionId(order) +
                "\",\"orderId\":" + receiveDto.getOrderId() +
                ",\"amount\":" + order.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\",\"crc\":\""+ (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }

    public static String createReceivedSign(NotificationReceiveDto receiveDto, Order order, PaymentMethodP24Config config) {
        String json = "{\"merchantId\":" + config.getMerchantId() +
                ",\"posId\":" + config.getPosId() +
                ",\"sessionId\":\"" + createSessionId(order) +
                "\",\"amount\":" + order.getGrossValue().movePointRight(2).intValue() +
                ",\"originAmount\":" + order.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\"" +
                ",\"orderId\":" + receiveDto.getOrderId() +
                ",\"methodId\":" + receiveDto.getMethodId() +
                ",\"statement\":\"" + receiveDto.getStatement() +
                "\",\"crc\":\"" + (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }

    private static String createSessionId(Order newOrder) {
        return "order_id_" + newOrder.getId().toString();
    }

    public static void validateIpAddress(String remoteAddr, PaymentMethodP24Config config) {
        if(!config.getServers().contains(remoteAddr)){
            throw new RuntimeException("Niepoprawny adres IP dla potwierdzenia płatności: " + remoteAddr);
        }
    }

    public static void validate(NotificationReceiveDto receiveDto, Order order, PaymentMethodP24Config config) {
        validateField(config.getMerchantId().equals(receiveDto.getMerchantId()));
        validateField(config.getPosId().equals(receiveDto.getPosId()));
        validateField(createSessionId(order).equals(receiveDto.getSessionId()));
        validateField(order.getGrossValue().compareTo(BigDecimal.valueOf(receiveDto.getAmount()).movePointLeft(2)) == 0);
        validateField(order.getGrossValue().compareTo(BigDecimal.valueOf(receiveDto.getOriginAmount()).movePointLeft(2)) == 0);
        validateField("PLN".equals(receiveDto.getCurrency()));
        validateField(createReceivedSign(receiveDto,order,config).equals(receiveDto.getSign()));
    }

    private static void validateField(boolean condition){
        if(!condition){
            throw new RuntimeException("Walidacja niepoprawna");
        }
    }
}
