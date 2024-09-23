package api.invext.bots.application.advice;

import api.invext.bots.domain.exception.InvalidServiceTypeException;
import api.invext.bots.domain.exception.RegisterNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RegisterNotFoundException.class)
    public ResponseEntity<Object> handleRegisterNotFoundException(RegisterNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(value = InvalidServiceTypeException.class)
    public ResponseEntity<Object> handleInvalidServiceTypeException(RegisterNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

}