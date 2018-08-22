package pl.netia.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.netia.gateway.model.UserForm;
import pl.netia.gateway.service.UserService;

@Component
public class Starter implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public Starter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.addAdmin(new UserForm("admin","admin@wp.pl","admin"));
    }
}