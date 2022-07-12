package ru.hartraien.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hartraien.userservice.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("UPDATE User u SET u.failedLoginAttempts = :failedAttempts WHERE  u.id=:id")
    @Modifying
    void updateFailedAttemptsById(@Param("id") Long id, @Param("failedAttempts") int i);

    @Query("UPDATE User u SET u.failedLoginAttempts = 0, u.locked = :lockStatus WHERE  u.id=:id")
    @Modifying
    void setUserLockById(@Param("id") Long id, @Param("lockStatus") boolean lockStatus);

    @Query("UPDATE User u SET u.failedLoginAttempts = u.failedLoginAttempts - 1 WHERE  u.id=:id")
    @Modifying
    void reduceFailedAttemptsById(@Param("id") Long id);

    @Query("UPDATE User u SET u.failedLoginAttempts=:failedLoginAttempts, u.locked = :lockStatus WHERE  u.id=:id")
    @Modifying
    void updateUserLockAndFailedAttempts(@Param("id") Long id, @Param("failedLoginAttempts") int failedLoginAttempts, @Param("lockStatus") boolean locked);
}
