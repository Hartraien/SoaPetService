package ru.hartraien.authservice.Service;

import ru.hartraien.authservice.DTOs.UsernameAndPasswordDTO;
import ru.hartraien.authservice.DTOs.UserServiceResponse;
import ru.hartraien.authservice.Exceptions.UserServiceConnectionException;
import ru.hartraien.authservice.Exceptions.UserServiceException;
import ru.hartraien.authservice.Exceptions.UserServiceFailedInputException;

public interface UserServiceConnector {

    UserServiceResponse register(UsernameAndPasswordDTO usernameAndPasswordDTO) throws UserServiceException, UserServiceConnectionException, UserServiceFailedInputException;

    UserServiceResponse loginUser(UsernameAndPasswordDTO usernameAndPasswordDTO) throws UserServiceException, UserServiceConnectionException, UserServiceFailedInputException;

    UserServiceResponse checkUserByUsernameAndId(long id, String username) throws UserServiceException, UserServiceConnectionException, UserServiceFailedInputException;
}
