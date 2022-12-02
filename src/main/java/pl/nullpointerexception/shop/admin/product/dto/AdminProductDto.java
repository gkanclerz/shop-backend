package pl.nullpointerexception.shop.admin.product.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Getter
public class AdminProductDto {
    private String name;
    private String category;
    private String description;
    private BigDecimal price;
    private String currency;
}
