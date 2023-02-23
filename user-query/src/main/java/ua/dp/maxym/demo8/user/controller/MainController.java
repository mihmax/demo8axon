package ua.dp.maxym.demo8.user.controller;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.dp.maxym.demo8.user.command.CreateUserCommand;
import ua.dp.maxym.demo8.user.query.User;
import ua.dp.maxym.demo8.user.query.UserRepository;

import java.util.List;

@RestController
public class MainController {

    private final CommandGateway commandGateway;
    private final UserRepository userRepo;

    @Autowired
    public MainController(CommandGateway commandGateway, UserRepository userRepo) {
        //this.userRepository = userRepository;
        this.commandGateway = commandGateway;
        this.userRepo = userRepo;
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/")
    public String index() {
        return """
                Call this once: <a href="/create">Create the repository</a></a>
                <br/>
                <a href="/list">List the repository</a>
                <br/><br/>
                <a href="/swagger-ui.html">Swagger for API</a>
                """;
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/create")
    public String create() {
        commandGateway.send(new CreateUserCommand("test1@gmail.com", "Test", "First", 1234.56));
        commandGateway.send(new CreateUserCommand("test2@gmail.com", "Test", "Second", 7890.12));
        commandGateway.send(new CreateUserCommand("test3@gmail.com", "Test", "Third", 3456.78));
        return """
                Created, see <a href="/list">the list of users</a>
                """;
    }

    @GetMapping("/list")
    public List<User> list() {
        return userRepo.findAll();
    }
}
