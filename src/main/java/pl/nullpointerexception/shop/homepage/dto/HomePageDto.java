package pl.nullpointerexception.shop.homepage.dto;

import pl.nullpointerexception.shop.common.model.Product;

import java.util.List;

public record HomePageDto(List<Product> saleProducts) {
}
