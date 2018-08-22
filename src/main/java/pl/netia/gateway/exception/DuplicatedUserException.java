package pl.netia.gateway.exception;

public class DuplicatedUserException extends RuntimeException {
    public DuplicatedUserException(String email) {
        super("Użytkownik o adresie email: " + email + " już istnieje");
    }
}
