package ru.hartraien.authservice.ControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.hartraien.authservice.DTOs.ErrorDTO;
import ru.hartraien.authservice.Exceptions.*;

@ControllerAdvice
public class ControllerExceptionAdvisor {

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionAdvisor.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserServiceFailedInputException.class)
    public ResponseEntity<ErrorDTO> handleWrongInputException(UserServiceFailedInputException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(exception.getMessage()));
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthTokenInvalidException.class)
    public ResponseEntity<ErrorDTO> handleTokenInvalidException(AuthTokenInvalidException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("Invalid authorization token"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthTokenExpiredException.class)
    public ResponseEntity<ErrorDTO> handleTokenExpiredException(AuthTokenExpiredException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AuthConnectionException.class)
    public ResponseEntity<ErrorDTO> handleConnectionException(AuthConnectionException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<ErrorDTO> handleConnectionException(AuthServiceException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO("Internal error, please, try again later"));
    }
}
