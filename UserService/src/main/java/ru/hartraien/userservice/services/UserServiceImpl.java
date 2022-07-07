package ru.hartraien.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hartraien.userservice.entities.User;
import ru.hartraien.userservice.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public User loginUser(String username, String rawPassword) throws UserServiceLoginException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword()))
                return user;
            else
                throw new UserServiceLoginException("Passwords do not match");
        } else
            throw new UserServiceLoginException("No such user by username");
    }

    @Override
    public User register(String username, String rawPassword) throws UserServiceLoginException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPassword));
            userRepository.save(user);
            return userRepository.findByUsername(username).get();
        } else
            throw new UserServiceLoginException("User with username " + username + " already exists");
    }

    @Override
    public User getUserInfo(String username) throws UserServiceLoginException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else
            throw new UserServiceLoginException("No such user by username");
    }

    @Override
    public boolean checkIfUserExists(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent();
    }
}
