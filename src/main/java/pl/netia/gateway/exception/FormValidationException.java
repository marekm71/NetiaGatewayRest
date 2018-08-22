package pl.netia.gateway.exception;

import org.springframework.validation.FieldError;

import java.util.List;

public class FormValidationException extends RuntimeException {

    private List<FieldError> fieldErrors;

    public FormValidationException(List<FieldError> fieldErrors) {
        super("Błędy w formularzu");
        this.fieldErrors = fieldErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
