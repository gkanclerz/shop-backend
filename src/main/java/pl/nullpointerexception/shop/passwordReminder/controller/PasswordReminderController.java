package pl.nullpointerexception.shop.passwordReminder.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.nullpointerexception.shop.passwordReminder.model.dto.PasswordChangeDto;
import pl.nullpointerexception.shop.passwordReminder.service.PasswordReminderService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class PasswordReminderController {

    private final PasswordReminderService passwordReminderService;

    @PostMapping("/passwordReminder")
    public void createReminderUrl(@RequestBody PasswordReminderDto passwordReminderDto){
        passwordReminderService.createReminderUrl(passwordReminderDto.getUsername());
    }

    @PutMapping("/passwordReminder")
    public void changePassword(@RequestBody @Valid PasswordChangeDto passwordChangeDto){
        passwordReminderService.changePassword(passwordChangeDto);
    }

    @Getter
    private static class PasswordReminderDto{
        private String username;
    }
}
