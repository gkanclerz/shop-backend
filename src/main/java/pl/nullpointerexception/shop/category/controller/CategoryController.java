package pl.nullpointerexception.shop.category.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nullpointerexception.shop.category.model.Category;
import pl.nullpointerexception.shop.category.model.CategoryProductsDto;
import pl.nullpointerexception.shop.category.service.CategoryService;

import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getCategories(){
        return categoryService.getCategories();
    }

    @GetMapping("/{slug}/products")
    public CategoryProductsDto getCategoriesWithProducts(@PathVariable @Pattern(regexp = "[a-z0-9\\-]+") @Length(max = 255) String slug,
                                                         Pageable pageable){
        return categoryService.getCategoriesWithProducts(slug, pageable);
    }

}
