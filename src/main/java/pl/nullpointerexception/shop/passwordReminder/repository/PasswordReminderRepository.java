package pl.nullpointerexception.shop.passwordReminder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.nullpointerexception.shop.passwordReminder.model.PasswordReminder;

import java.util.Optional;


public interface PasswordReminderRepository extends JpaRepository<PasswordReminder,Long> {
    Optional<PasswordReminder> findByLink(String link);
}
