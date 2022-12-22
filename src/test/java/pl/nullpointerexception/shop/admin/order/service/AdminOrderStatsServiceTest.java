package pl.nullpointerexception.shop.admin.order.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.nullpointerexception.shop.admin.order.model.AdminOrder;
import pl.nullpointerexception.shop.admin.order.model.dto.AdminOrdersStats;
import pl.nullpointerexception.shop.admin.order.repository.AdminOrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminOrderStatsServiceTest {

    @InjectMocks
    private AdminOrderStatsService adminOrderStatsService;
    @Mock
    private AdminOrderRepository adminOrderRepository;

    @Test
    void should_corectly_aggregate_values() {
        //given
        List<AdminOrder> orders = createAdminOrders();
        when(adminOrderRepository.findAllByPlaceDateIsBetweenAndOrderStatus(any(),any(),any())).thenReturn(orders);
        //when
        AdminOrdersStats result = adminOrderStatsService.getStatistics();
        //then
        Assertions.assertThat(result.getOrder().get(0)).isEqualTo(2L);
        Assertions.assertThat(result.getOrder().get(1)).isEqualTo(2L);
        Assertions.assertThat(result.getSale().get(0)).isEqualTo(new BigDecimal("61.00"));
        Assertions.assertThat(result.getSale().get(1)).isEqualTo(new BigDecimal("40.00"));
        Assertions.assertThat(result.getOrdersCount()).isEqualTo(5L);
        Assertions.assertThat(result.getSalesSum()).isEqualTo(new BigDecimal("121.00"));
    }

    private static List<AdminOrder> createAdminOrders() {
        return List.of(AdminOrder.builder()
                        .grossValue(new BigDecimal("50.99"))
                        .placeDate(LocalDateTime.now().withDayOfMonth(1))
                        .build(),
                AdminOrder.builder()
                        .grossValue(new BigDecimal("10.01"))
                        .placeDate(LocalDateTime.now().withDayOfMonth(1))
                        .build(),
                AdminOrder.builder()
                        .grossValue(new BigDecimal("20.00"))
                        .placeDate(LocalDateTime.now().withDayOfMonth(2))
                        .build(),
                AdminOrder.builder()
                        .grossValue(new BigDecimal("20.00"))
                        .placeDate(LocalDateTime.now().withDayOfMonth(2))
                        .build(),
                AdminOrder.builder()
                        .grossValue(new BigDecimal("20.00"))
                        .placeDate(LocalDateTime.now().withDayOfMonth(3))
                        .build()
        );
    }
}