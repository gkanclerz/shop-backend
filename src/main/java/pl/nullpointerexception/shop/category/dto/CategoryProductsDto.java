package pl.nullpointerexception.shop.category.dto;

import org.springframework.data.domain.Page;
import pl.nullpointerexception.shop.common.model.Category;
import pl.nullpointerexception.shop.common.dto.ProductListDto;

public record CategoryProductsDto(Category category, Page<ProductListDto> products) {
}
