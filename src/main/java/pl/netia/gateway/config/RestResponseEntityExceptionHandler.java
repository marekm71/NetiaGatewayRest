package pl.netia.gateway.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.netia.gateway.exception.DuplicatedUserException;
import pl.netia.gateway.exception.FormValidationException;

import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleNotFound(DuplicatedUserException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> formValidation(FormValidationException ex) {
        StringBuilder builder = new StringBuilder();
        List<FieldError> errors = ex.getFieldErrors();
        for (FieldError error : errors) {
            builder.append(error.getField())
                    .append(" : ")
                    .append(error.getDefaultMessage())
                    .append("\n");
        }
        return new ResponseEntity<>(builder.toString(), HttpStatus.BAD_REQUEST);
    }
}
