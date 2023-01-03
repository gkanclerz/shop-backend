package pl.nullpointerexception.shop.passwordReminder.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PasswordChangeDto {
    @NotBlank
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String link;
}
