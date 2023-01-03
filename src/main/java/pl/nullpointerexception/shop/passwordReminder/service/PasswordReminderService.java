package pl.nullpointerexception.shop.passwordReminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nullpointerexception.shop.common.mail.EmailClientService;
import pl.nullpointerexception.shop.common.model.User;
import pl.nullpointerexception.shop.common.repository.UserRepository;
import pl.nullpointerexception.shop.passwordReminder.model.PasswordReminder;
import pl.nullpointerexception.shop.passwordReminder.model.dto.PasswordChangeDto;
import pl.nullpointerexception.shop.passwordReminder.repository.PasswordReminderRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordReminderService {

    private final UserRepository userRepository;
    private final PasswordReminderRepository passwordReminderRepository;
    private final EmailClientService emailClientService;
    @Value("${app.serviceAddress}")
    private String serviceAddress;

    public void createReminderUrl(String username) {
        userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("Użytkownik o podanym mailu nie istnieje!"));
        PasswordReminder passwordReminder;
        try {
            passwordReminder = passwordReminderRepository.save(PasswordReminder.builder()
                    .username(username)
                    .placeDate(LocalDateTime.now())
                    .link(UUID.randomUUID().toString())
                    .build());
        } catch (Exception e) {
            throw new IllegalArgumentException("Na podany mail został już wysłany email. Sprawdź maila.");
        }
        emailClientService.getInstance().send(username, "Prośba o zmiane hasła - shop", createEmailMessage(passwordReminder));
    }

    @Transactional
    public void changePassword(PasswordChangeDto passwordChangeDto) {
        if(!passwordChangeDto.getPassword().equals(passwordChangeDto.getRepeatPassword())){
            throw new IllegalArgumentException("Hasła nie są identyczne!");
        }
        PasswordReminder passwordReminder = passwordReminderRepository.findByLink(passwordChangeDto.getLink())
                .orElseThrow(() -> new NoSuchElementException("Nieprawidłowy link!"));
        User user = userRepository.findByUsername(passwordReminder.getUsername()).orElseThrow();
        user.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(passwordChangeDto.getPassword()));
        passwordReminderRepository.delete(passwordReminder);
    }

    private String createEmailMessage(PasswordReminder passwordReminder) {
        return "Link do zmiany hasła: "+serviceAddress + "/passwordReset/" + passwordReminder.getLink();
    }
}
