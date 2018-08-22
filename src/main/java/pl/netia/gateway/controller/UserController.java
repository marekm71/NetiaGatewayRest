package pl.netia.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.netia.gateway.model.AppUser;
import pl.netia.gateway.model.UserFormEmail;
import pl.netia.gateway.exception.FormValidationException;
import pl.netia.gateway.model.PasswordResetForm;
import pl.netia.gateway.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody @Valid UserFormEmail userFormEmail, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new FormValidationException(bindingResult.getFieldErrors());
        }
        userService.addUser(userFormEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/check-token")
    public ResponseEntity checkToken(@RequestBody String token){
        Optional<AppUser> user = userService.findUserByResetToken(token);
        if(user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity changePassword(@RequestBody @Valid PasswordResetForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new FormValidationException(bindingResult.getFieldErrors());
        }
        AppUser user = userService.findUserByResetToken(form.getToken()).get();
        user.setPassword(bCryptPasswordEncoder.encode(form.getPassword()));
        user.setResetToken(null);
        userService.update(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}