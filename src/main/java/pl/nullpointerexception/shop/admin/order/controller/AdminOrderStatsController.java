package pl.nullpointerexception.shop.admin.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nullpointerexception.shop.admin.order.model.dto.AdminOrdersStats;
import pl.nullpointerexception.shop.admin.order.service.AdminOrderStatsService;

@RestController
@RequestMapping("/admin/orders/stats")
@RequiredArgsConstructor
public class AdminOrderStatsController {

    private final AdminOrderStatsService adminOrderStatsService;

    @GetMapping
    public AdminOrdersStats getOrderStatistics(){
        return adminOrderStatsService.getStatistics();
    }
}
