package pl.nullpointerexception.shop.order.service.payment.p24;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import pl.nullpointerexception.shop.order.model.Order;
import reactor.core.publisher.Mono;

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
                        .urlReturn(config.isTestMode() ? config.getTestUrlReturn() : config.getUrlReturn())
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
}
