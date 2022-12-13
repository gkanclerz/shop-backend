package pl.nullpointerexception.shop.category.model;

import org.springframework.data.domain.Page;
import pl.nullpointerexception.shop.product.controller.dto.ProductListDto;

public record CategoryProductsDto(Category category, Page<ProductListDto> products) {
}
