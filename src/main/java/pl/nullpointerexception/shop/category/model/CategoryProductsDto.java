package pl.nullpointerexception.shop.category.model;

import org.springframework.data.domain.Page;
import pl.nullpointerexception.shop.product.model.Product;

public record CategoryProductsDto(Category category, Page<Product> products) {
}
