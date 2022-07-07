package ru.hartraien.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hartraien.userservice.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
