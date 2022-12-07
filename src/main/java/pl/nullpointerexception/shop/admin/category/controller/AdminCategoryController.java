package pl.nullpointerexception.shop.admin.category.controller;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.nullpointerexception.shop.admin.category.dto.AdminCategoryDto;
import pl.nullpointerexception.shop.admin.category.model.AdminCategory;
import pl.nullpointerexception.shop.admin.category.service.AdminCategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    public static final Long EMPTY_ID = null;
    private final AdminCategoryService adminCategoryService;

    @GetMapping
    public List<AdminCategory> getCategories(){
        return adminCategoryService.getCategories();
    }

    @GetMapping("/{id}")
    public AdminCategory getCategory(@PathVariable Long id){
        return adminCategoryService.getCategory(id);
    }

    @PostMapping
    public AdminCategory createCategory(@RequestBody @Valid AdminCategoryDto adminCategoryDto){
        return adminCategoryService.createCategory(mapToAdminCategory(EMPTY_ID, adminCategoryDto));
    }

    private AdminCategory mapToAdminCategory(Long id, AdminCategoryDto adminCategoryDto) {
        return AdminCategory.builder()
                .id(id)
                .name(adminCategoryDto.getName())
                .description(adminCategoryDto.getDescription())
                .slug(slugifyCategoryName(adminCategoryDto.getSlug()))
                .build();
    }

    private String slugifyCategoryName(String slug) {
        Slugify slugify = new Slugify();
        return slugify.withCustomReplacement("_","-")
                .slugify(slug);
    }

    @PutMapping("/{id}")
    public AdminCategory updateCategory(@PathVariable Long id, @RequestBody @Valid AdminCategoryDto adminCategoryDto){
        return adminCategoryService.updateCategory(mapToAdminCategory(id,adminCategoryDto));
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        adminCategoryService.deleteCategory(id);
    }
}
