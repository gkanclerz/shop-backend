package pl.nullpointerexception.shop.order.service.payment.p24;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import pl.nullpointerexception.shop.order.model.Order;
import pl.nullpointerexception.shop.order.model.dto.NotificationReceiveDto;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentMethodP24 {

    private final PaymentMethodP24Config config;

    public String initPayment(Order newOrder){
        log.info("Inicjalizacja płatności");

        WebClient webClient = WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(config.getPosId().toString(),
                        config.isTestMode() ? config.getTestSecretKey() : config.getSecretKey()))
                .baseUrl(config.isTestMode() ? config.getTestApiUrl() : config.getApiUrl())
                .build();

        ResponseEntity<TransatcionRegisterResponse> result = webClient.post().uri("/transaction/register")
                .bodyValue(TransactionRegisterRequest.builder()
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
                        .urlReturn(getUrlReturn(newOrder.getOrderHash()))
                        .urlStatus(generateStatusUrl(newOrder.getOrderHash()))
                        .sign(createSign(newOrder))
                        .encoding("UTF-8")
                        .build())
                .retrieve()
                .onStatus(httpStatus -> {
                    log.error("Coś poszło źle: " + httpStatus.name());
                    return httpStatus.is4xxClientError();
                }, clientResponse -> Mono.empty())
                .toEntity(TransatcionRegisterResponse.class)
                .block();
        if(result != null && result.getBody() != null && result.getBody().getData() != null){
            return (config.isTestMode() ? config.getTestUrl() : config.getUrl()) + "/trnRequest/" +
                    result.getBody().getData().token();
        }
        return null;
    }

    public String receiveNotification(Order order, NotificationReceiveDto receiveDto, String remoteAddr) {
        log.info(receiveDto.toString());
        //validateIpAddress(remoteAddr);
        validate(receiveDto,order);
        return verifyPayment(receiveDto,order);
    }

    private void validateIpAddress(String remoteAddr) {
        if(!config.getServers().contains(remoteAddr)){
            throw new RuntimeException("Niepoprawny adres IP dla potwierdzenia płatności: " + remoteAddr);
        }
    }

    private String verifyPayment(NotificationReceiveDto receiveDto, Order order) {
        WebClient webClient = WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(config.getPosId().toString(),
                        config.isTestMode() ? config.getTestSecretKey() : config.getSecretKey()))
                .baseUrl(config.isTestMode() ? config.getTestApiUrl() : config.getApiUrl())
                .build();
        ResponseEntity<TransatcionVerifyResponse> result = webClient.put().uri("/transaction/verify")
                .bodyValue(TransactionVerifyRequest.builder()
                        .merchantId(config.getMerchantId())
                        .posId(config.getPosId())
                        .sessionId(createSessionId(order))
                        .amount(order.getGrossValue().movePointRight(2).intValue())
                        .currency("PLN")
                        .orderId(receiveDto.getOrderId())
                        .sign(createVerifySign(receiveDto, order))
                        .build())
                .retrieve()
                .toEntity(TransatcionVerifyResponse.class)
                .block();
        log.info("Weryfikacja transakcji status: " + result.getBody().getData().status());
        return result.getBody().getData().status();
    }

    private String createVerifySign(NotificationReceiveDto receiveDto, Order order) {
        String json = "{\"sessionId\":\"" + createSessionId(order) +
                "\",\"orderId\":" + receiveDto.getOrderId() +
                ",\"amount\":" + order.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\",\"crc\":\""+ (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }

    private String generateStatusUrl(String orderHash) {
        String baseUrl = config.isTestMode() ? config.getTestUrlStatus() : config.getUrlStatus();
        return baseUrl + "/orders/notification/" + orderHash;
    }

    private String getUrlReturn(String orderHash) {
        String baseUrl = config.isTestMode() ? config.getTestUrlReturn() : config.getUrlReturn();
        return baseUrl + "/order/notification/" + orderHash;
    }

    private String createSign(Order newOrder) {
        String json = "{\"sessionId\":\"" + createSessionId(newOrder) +
                "\",\"merchantId\":" + config.getMerchantId() +
                ",\"amount\":" + newOrder.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\",\"crc\":\""+ (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }

    private String createSessionId(Order newOrder) {
        return "order_id_" + newOrder.getId().toString();
    }

    private void validate(NotificationReceiveDto receiveDto, Order order) {
        validateField(config.getMerchantId().equals(receiveDto.getMerchantId()));
        validateField(config.getPosId().equals(receiveDto.getPosId()));
        validateField(createSessionId(order).equals(receiveDto.getSessionId()));
        validateField(order.getGrossValue().compareTo(BigDecimal.valueOf(receiveDto.getAmount()).movePointLeft(2)) == 0);
        validateField(order.getGrossValue().compareTo(BigDecimal.valueOf(receiveDto.getOriginAmount()).movePointLeft(2)) == 0);
        validateField("PLN".equals(receiveDto.getCurrency()));
        validateField(createReceivedSign(receiveDto,order).equals(receiveDto.getSign()));
    }

    private String createReceivedSign(NotificationReceiveDto receiveDto, Order order) {
        String json = "{\"merchantId\":" + config.getMerchantId() +
                "\",\"posId\":" + config.getPosId() +
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

    private void validateField(boolean condition){
        if(!condition){
            throw new RuntimeException("Walidacja niepoprawna");
        }
    }
}
