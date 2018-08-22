package pl.netia.gateway.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
public class PasswordResetForm {
    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmPassword;

    @NotEmpty
    private String token;

    @AssertTrue(message="Hasła muszą być identyczne")
    private boolean isValid(){
        if (password == null) {
            return confirmPassword == null;
        } else {
            return password.equals(confirmPassword);
        }
    }
}