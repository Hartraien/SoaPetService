package ru.hartraien.userservice.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hartraien.userservice.repositories.UserRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserLockServiceImpl implements UserLockService {
    private final UserRepository userRepository;

    public UserLockServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void reduceFailedLoginAttemptsById(Long id) {
        userRepository.reduceFailedAttemptsById(id);
    }

    @Override
    public void unlockUserById(Long id) {
        userRepository.setUserLockById(id, false);
    }

    @Override
    public void updateUserLockAndFailedAttempts(Long id, int failedLoginAttempts, boolean locked) {
        userRepository.updateUserLockAndFailedAttempts(id, failedLoginAttempts, locked);
    }

    @Override
    public void updateFailedAttemptsById(Long id, int attempts) {
        userRepository.updateFailedAttemptsById(id, attempts);
    }
}
