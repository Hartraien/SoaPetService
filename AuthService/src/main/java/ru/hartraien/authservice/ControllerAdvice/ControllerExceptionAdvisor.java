package ru.hartraien.authservice.ControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.hartraien.authservice.DTOs.ErrorDTO;
import ru.hartraien.authservice.Exceptions.*;

@ControllerAdvice
public class ControllerExceptionAdvisor {

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionAdvisor.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public ControllerExceptionAdvisor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserServiceFailedInputException.class)
    public ResponseEntity<ErrorDTO> handleWrongInputException(UserServiceFailedInputException exception) {
        logger.error(exception.getMessage(), exception);
        ErrorDTO errorDTO = new ErrorDTO(exception.getMessage());
        try {
            logger.debug(exception.getMessage());
            errorDTO = objectMapper.readValue(exception.getMessage(), ErrorDTO.class);
        } catch (JsonMappingException e) {
            logger.error("Could not map", e);
        } catch (JsonProcessingException e) {
            logger.error("Could not process json", e);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthTokenInvalidException.class)
    public ResponseEntity<ErrorDTO> handleTokenInvalidException(AuthTokenInvalidException exception) {
        logger.debug("Invalid token because: " + exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("Invalid authorization token"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthTokenExpiredException.class)
    public ResponseEntity<ErrorDTO> handleTokenExpiredException(AuthTokenExpiredException exception) {
        logger.debug("Token expired because: " + exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AuthConnectionException.class)
    public ResponseEntity<ErrorDTO> handleConnectionException(AuthConnectionException exception) {
        logger.debug("Connection failed because: " + exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<ErrorDTO> handleConnectionException(AuthServiceException exception) {
        logger.debug("Service error: " + exception.getMessage());
        logger.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO("Internal error, please, try again later"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException exception) {
        logger.warn(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }
}
