package ru.practicum.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

}
