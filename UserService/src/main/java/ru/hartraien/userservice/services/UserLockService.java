package ru.hartraien.userservice.services;

public interface UserLockService {
    void reduceFailedLoginAttemptsById(Long id);

    void unlockUserById(Long id);

    void updateUserLockAndFailedAttempts(Long id, int failedLoginAttempts, boolean locked);

    void updateFailedAttemptsById(Long id, int attempts);
}
