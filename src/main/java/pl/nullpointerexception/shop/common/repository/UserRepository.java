package pl.nullpointerexception.shop.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.nullpointerexception.shop.common.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
