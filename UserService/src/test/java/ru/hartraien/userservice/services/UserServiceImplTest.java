package ru.hartraien.userservice.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.hartraien.userservice.entities.User;
import ru.hartraien.userservice.exceptions.UserServiceLoginException;
import ru.hartraien.userservice.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;


class UserServiceImplTest {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    private UserLockService userLockService;

    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        passwordEncoder = new BCryptPasswordEncoder();
        userRepository = Mockito.mock(UserRepository.class);
        userLockService = Mockito.mock(UserLockService.class);
        userService = new UserServiceImpl(passwordEncoder, userRepository, userLockService, 60, 10);
    }

    @Test
    void loginUser_Valid() {
        Long id = 1L;
        String username = "username";
        String password = "password";
        int failedLoginAttempts = 0;
        boolean locked = false;
        LocalDateTime unlockTime = null;
        User user = generateUser(id, username, password, failedLoginAttempts, locked, unlockTime);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        try {
            Assertions.assertSame(user, userService.loginUser(username, password));
        } catch (UserServiceLoginException e) {
            Assertions.fail(e.getMessage());
        }
    }

    private User generateUser(Long id, String username, String password, int failedLoginAttempts, boolean locked, LocalDateTime unlockTime) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFailedLoginAttempts(failedLoginAttempts);
        user.setLocked(locked);
        user.setUnlockTime(unlockTime);
        return user;
    }

    @Test
    void loginUser_WrongPassword() {
        Long id = 1L;
        String username = "username";
        String password = "password";
        int failedLoginAttempts = 0;
        boolean locked = false;
        LocalDateTime unlockTime = null;
        User user = generateUser(id, username, password, failedLoginAttempts, locked, unlockTime);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        String wrongPassword = "wrongPassword";

        Assertions.assertThrows(UserServiceLoginException.class, () -> userService.loginUser(username, wrongPassword));
        try {
            userService.loginUser(username, wrongPassword);
            Assertions.fail("Logged user with wrong password");
        } catch (UserServiceLoginException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("wrong password"), "Instead got message = " + e.getMessage());
        }
    }

    @Test
    void loginUser_NonExisting() {
        String username = "username";
        String password = "password";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserServiceLoginException.class, () -> userService.loginUser(username, password));
        try {
            userService.loginUser(username, password);
            Assertions.fail("Logged non existing user");
        } catch (UserServiceLoginException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("no account with"), "Instead got message = " + e.getMessage());
        }
    }

    @Test
    void loginUser_Locked() {
        Long id = 1L;
        String username = "username";
        String password = "password";
        int failedLoginAttempts = 10;
        boolean locked = true;
        LocalDateTime unlockTime = LocalDateTime.now().plusSeconds(60);
        User user = generateUser(id, username, password, failedLoginAttempts, locked, unlockTime);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));


        Assertions.assertThrows(UserServiceLoginException.class, () -> userService.loginUser(username, password));
        try {
            userService.loginUser(username, password);
            Assertions.fail("Logged locked user");
        } catch (UserServiceLoginException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("locked"), "Instead got message = " + e.getMessage());
        }
    }

    @Test
    void register_Existing() {
        Long id = 1L;
        String username = "username";
        String password = "password";
        int failedLoginAttempts = 0;
        boolean locked = false;
        LocalDateTime unlockTime = null;
        User user = generateUser(id, username, password, failedLoginAttempts, locked, unlockTime);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Assertions.assertThrows(UserServiceLoginException.class, () -> userService.register(username, password));
        try {
            userService.register(username, password);
            Assertions.fail("Logged locked user");
        } catch (UserServiceLoginException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("exists"), "Instead got message = " + e.getMessage());
        }
    }

    @Test
    void register_NonExisting() {
        Long id = 1L;
        String username = "username";
        String password = "password";
        int failedLoginAttempts = 0;
        boolean locked = false;
        LocalDateTime unlockTime = null;
        User user = generateUser(id, username, password, failedLoginAttempts, locked, unlockTime);

        Mockito.doAnswer(new Answer() {
            private boolean firstTime = true;

            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (firstTime) {
                    firstTime = false;
                    return Optional.empty();
                } else
                    return Optional.of(user);
            }
        }).when(userRepository).findByUsername(username);

        try {
            Assertions.assertSame(user, userService.register(username, password));
        } catch (UserServiceLoginException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void getUserInfo_Existing() {
        Long id = 1L;
        String username = "username";
        String password = "password";
        int failedLoginAttempts = 0;
        boolean locked = false;
        LocalDateTime unlockTime = null;
        User user = generateUser(id, username, password, failedLoginAttempts, locked, unlockTime);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        try {
            Assertions.assertSame(user, userService.getUserInfo(username));
        } catch (UserServiceLoginException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void getUserInfo_NonExisting() {
        String username = "username";
        String password = "password";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserServiceLoginException.class, () -> userService.getUserInfo(username));
        try {
            userService.getUserInfo(username);
            Assertions.fail("Returned nonexistent user");
        } catch (UserServiceLoginException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("no account with"), "Instead got message = " + e.getMessage());
        }
    }

    @Test
    void checkIfUserExists_Existing() {
        Long id = 1L;
        String username = "username";
        String password = "password";
        int failedLoginAttempts = 0;
        boolean locked = false;
        LocalDateTime unlockTime = null;
        User user = generateUser(id, username, password, failedLoginAttempts, locked, unlockTime);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Assertions.assertTrue(userService.checkIfUserExists(username));
    }

    @Test
    void checkIfUserExists_NonExisting() {
        String username = "username";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Assertions.assertFalse(userService.checkIfUserExists(username));
    }
}