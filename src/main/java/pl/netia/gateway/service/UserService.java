package pl.netia.gateway.service;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.netia.gateway.emailconfig.EmailSender;
import pl.netia.gateway.exception.DuplicatedUserException;
import pl.netia.gateway.model.AppUser;
import pl.netia.gateway.model.UserForm;
import pl.netia.gateway.model.UserFormEmail;
import pl.netia.gateway.repository.UserRepository;
import pl.netia.gateway.model.Role;
import org.springframework.core.env.Environment;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final TemplateEngine templateEngine;
    private final Environment environment;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, EmailSender emailSender, TemplateEngine templateEngine, Environment environment) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.environment = environment;
    }

    @Transactional
    public void addUser(UserFormEmail userFormEmail){
        if(isDuplicated(userFormEmail)){
            throw new DuplicatedUserException(userFormEmail.getEmail());
        }
        AppUser newAppUser = new AppUser();
        String loginTemp = RandomStringUtils.random(6,false,true);
        String passwordTemp = RandomStringUtils.random(6,true,true);
        newAppUser.setResetToken(UUID.randomUUID().toString());
        newAppUser.setUsername(loginTemp);
        newAppUser.setEmail(userFormEmail.getEmail());
        newAppUser.setPassword(passwordEncoder.encode(passwordTemp));
        String linkToResetPassword = environment.getProperty("email.link") + newAppUser.getResetToken();
        Role role = new Role("ROLE_USER");
        role.setAppUser(newAppUser);
        newAppUser.addNewRoles(role);
        userRepository.save(newAppUser);
        send(userFormEmail.getEmail(),loginTemp,linkToResetPassword);
    }

    @Transactional
    public void addAdmin(UserForm userForm){
        AppUser newAppUser = new AppUser();
        newAppUser.setUsername(userForm.getUsername());
        newAppUser.setEmail(userForm.getEmail());
        newAppUser.setPassword(passwordEncoder.encode(userForm.getPassword()));
        Role role = new Role("ROLE_ADMIN");
        role.setAppUser(newAppUser);
        newAppUser.addNewRoles(role);
        userRepository.save(newAppUser);
    }

    private boolean isDuplicated(UserFormEmail userFormEmail) {
        int count = userRepository.countByEmail(userFormEmail.getEmail());
        return count != 0;
    }

    public Optional<AppUser> findUserByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }

    @Transactional
    public void update(AppUser appUser){
        userRepository.save(appUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new org.springframework.security.core.userdetails.User(
                appUser.getUsername(),
                appUser.getPassword(),
                appUser.getAuthorities()
        );
    }

    private void send(String to, String login, String linkToResetPassword) {
        Context context = new Context();
        context.setVariable("login", login);
        context.setVariable("link", linkToResetPassword );
        String body = templateEngine.process("template", context);
        emailSender.sendEmail(environment.getProperty("spring.mail.username"),to, environment.getProperty("email.subject"), body);
    }
}
