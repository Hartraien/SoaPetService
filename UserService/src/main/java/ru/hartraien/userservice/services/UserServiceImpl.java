package ru.hartraien.userservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hartraien.userservice.entities.User;
import ru.hartraien.userservice.exceptions.UserServiceLoginException;
import ru.hartraien.userservice.repositories.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final UserLockService userLockService;

    private final ScheduledExecutorService scheduledExecutorService;

    private final long LOCK_TIME_SECONDS;

    private final int MAX_ALLOWED_LOGIN_ATTEMPTS;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder
            , UserRepository userRepository
            , UserLockService userLockService, @Value("${application.loginFailedAttemptSeconds}") long lockTimeSeconds
            , @Value("${application.loginFailedAttemptLimit}") int max_allowed_login_attempts) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userLockService = userLockService;
        LOCK_TIME_SECONDS = lockTimeSeconds;
        MAX_ALLOWED_LOGIN_ATTEMPTS = max_allowed_login_attempts;
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    }

    @Transactional
    @Override
    public User loginUser(String username, String rawPassword) throws UserServiceLoginException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.isLocked()) {
                String errorMessage = "Account is locked, try again in " + getTimeToUnlockInMinutes(user) + " minutes";
                logger.debug(errorMessage);
                throw new UserServiceLoginException(errorMessage);
            }
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                resetLoginFailures(user);
                return user;
            } else {
                String errorMessage = "Wrong password";
                updateUserFailedAttempts(user);
                if (user.isLocked())
                    errorMessage += " Too many failed attempts, account locked for " + convertLockTimeToMinutes(LOCK_TIME_SECONDS) + " minutes";
                logger.debug(errorMessage);
                throw new UserServiceLoginException(errorMessage);
            }
        } else {
            String formattedOutput = String.format("No account with username '%s'", username);
            logger.debug(formattedOutput);
            throw new UserServiceLoginException(formattedOutput);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User register(String username, String rawPassword) throws UserServiceLoginException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPassword));
            userRepository.save(user);
            return userRepository.findByUsername(username).get();
        } else {
            String formattedOutput = String.format("Account with username '%s' already exists", username);
            logger.debug(formattedOutput);
            throw new UserServiceLoginException(formattedOutput);
        }
    }

    @Override
    public User getUserInfo(String username) throws UserServiceLoginException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            String formattedOutput = String.format("No account with username '%s'", username);
            logger.debug(formattedOutput);
            throw new UserServiceLoginException(formattedOutput);
        }
    }

    @Override
    public boolean checkIfUserExists(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        String formattedOutput = String.format("Account with username '%s' %s", username, (userOptional.isPresent() ? "exists" : "does not exist"));
        logger.debug(formattedOutput);
        return userOptional.isPresent();
    }

    private String getTimeToUnlockInMinutes(User user) {
        logger.debug("Calculating time until unlock");
        LocalDateTime unlockTime = user.getUnlockTime();
        LocalDateTime now = LocalDateTime.now();
        long diffInMinutes = Duration.between(now, unlockTime).toMinutes();
        return String.valueOf(diffInMinutes);
    }

    private String convertLockTimeToMinutes(long lockTimeSeconds) {
        return String.valueOf(lockTimeSeconds / 60);
    }

    private void updateUserFailedAttempts(User user) {
        logger.debug("User " + user.getUsername() + " failed login attempts to " + user.getFailedLoginAttempts());
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        logger.debug("Set user " + user.getUsername() + " failed login attempts to " + user.getFailedLoginAttempts());
        if (user.getFailedLoginAttempts() >= MAX_ALLOWED_LOGIN_ATTEMPTS) {
            logger.debug("Locked user " + user.getUsername());
            user.setLocked(true);
            LocalDateTime unlockTime = LocalDateTime.now().plusSeconds(LOCK_TIME_SECONDS);
            user.setUnlockTime(unlockTime);
        }
        logger.debug("before = " + userRepository.findById(user.getId()));
        userLockService.updateUserLockAndFailedAttempts(user.getId(), user.getFailedLoginAttempts(), user.isLocked());
        logger.debug("after = " + userRepository.findById(user.getId()));

        scheduledExecutorService.schedule(() -> {
                    logger.debug("Reducing user's " + user.getUsername() + " failed login attempts by 1.");
                    logger.debug("before = " + userRepository.findById(user.getId()));
                    userLockService.reduceFailedLoginAttemptsById(user.getId());
                    logger.debug("after = " + userRepository.findById(user.getId()));
                }
                , LOCK_TIME_SECONDS, TimeUnit.SECONDS);
        if (user.isLocked()) {
            scheduledExecutorService.schedule(() -> {
                        logger.debug("Unlocking User " + user.getUsername());
                        logger.debug("before = " + userRepository.findById(user.getId()));
                        userLockService.unlockUserById(user.getId());
                        logger.debug("after = " + userRepository.findById(user.getId()));
                    }
                    , LOCK_TIME_SECONDS, TimeUnit.SECONDS);
        }
    }

    private void resetLoginFailures(User user) {
        userLockService.updateFailedAttemptsById(user.getId(), 0);
    }
}
