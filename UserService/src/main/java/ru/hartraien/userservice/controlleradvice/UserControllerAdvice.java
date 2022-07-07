package ru.hartraien.userservice.controlleradvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.hartraien.userservice.services.UserServiceLoginException;

@ControllerAdvice
public class UserControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(UserControllerAdvice.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserServiceLoginException.class)
    public ResponseEntity<ErrorDTO> handlWrongInputException(UserServiceLoginException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(exception.getMessage()));
    }
}
