package ru.hartraien.userservice.services;


import ru.hartraien.userservice.entities.User;
import ru.hartraien.userservice.exceptions.UserServiceLoginException;

public interface UserService {
    User loginUser(String username, String password) throws UserServiceLoginException;

    User register(String username, String password) throws UserServiceLoginException;

    User getUserInfo(String username) throws UserServiceLoginException;

    boolean checkIfUserExists(String username);
}
