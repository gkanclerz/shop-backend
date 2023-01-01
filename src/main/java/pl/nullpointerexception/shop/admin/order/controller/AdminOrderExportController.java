package pl.nullpointerexception.shop.admin.order.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.nullpointerexception.shop.admin.order.model.AdminOrder;
import pl.nullpointerexception.shop.admin.order.service.AdminExportService;
import pl.nullpointerexception.shop.common.model.OrderStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/orders/export")
public class AdminOrderExportController {

    private static final CSVFormat FORMAT = CSVFormat.Builder
            .create(CSVFormat.DEFAULT)
            .setHeader("Id", "PlaceDate", "OrderStatus", "GrossValue", "Firstname",
                    "Lastname", "Street", "Zipcode", "City",
                    "Email", "Phone", "Payment")
            .build();

    private final AdminExportService adminExportService;

    @GetMapping
    public ResponseEntity<Resource> exportOrders(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to,
            @RequestParam OrderStatus orderStatus
    ){
        List<AdminOrder> adminOrders = adminExportService.exportOrders(
                LocalDateTime.of(from, LocalTime.of(0,0,0)),
                LocalDateTime.of(to, LocalTime.of(23,59,59)),
                orderStatus);
        ByteArrayInputStream stream = transformToCsv(adminOrders);
        InputStreamResource resource = new InputStreamResource(stream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "orderExport.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    private ByteArrayInputStream transformToCsv(List<AdminOrder> adminOrders){
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
            CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), FORMAT)){
                for (AdminOrder order : adminOrders) {
                    printer.printRecord(Arrays.asList(
                            order.getId(),
                            order.getPlaceDate(),
                            order.getOrderStatus().getValue(),
                            order.getGrossValue(),
                            order.getFirstname(),
                            order.getLastname(),
                            order.getStreet(),
                            order.getZipcode(),
                            order.getCity(),
                            order.getEmail(),
                            order.getPhone(),
                            order.getPayment().getName()
                    ));
                }
            printer.flush();
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Błąd przetwarzania CSV: " + e.getMessage());
        }
    }
}
