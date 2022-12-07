package pl.nullpointerexception.shop.admin.category.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public class AdminCategoryDto {
    @NotBlank
    @Length(min = 4)
    private String name;
    private String description;
    @NotBlank
    @Length(min = 4)
    private String slug;
}
