package ua.dp.maxym.demo8.user.controller;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.unitofwork.DefaultUnitOfWork;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.modelling.command.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.dp.maxym.demo8.common.util.AxonUtil;
import ua.dp.maxym.demo8.user.aggregate.UserAggregate;
import ua.dp.maxym.demo8.user.command.CreateUserCommand;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
public class MainController {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    Repository<UserAggregate> userRepository;
    @Autowired
    private CommandGateway commandGateway;

    @GetMapping("/")
    public String index() {
        return """
                Call this once: <a href="/create">Create the repository</a></a>
                <br/>
                <a href="/list">List the repository</a>
                """;
    }

    @GetMapping("/create")
    public String create() {
        commandGateway.send(new CreateUserCommand("mihmax@gmail.com", "Maxym", "Mykhalchuk", 1234.56));
        commandGateway.send(new CreateUserCommand("mmyk@ciklum.com", "Maksym", "Mykhalchuk", 7890.12));
        commandGateway.send(new CreateUserCommand("test@gmail.com", "Test", "User", 3456.78));
        return """
                Created, see <a href="/list">the list of users</a>
                """;
    }

    @GetMapping("/list")
    public Map<String, List<UserAggregate>> list() {
        UnitOfWork<Message<?>> uow = DefaultUnitOfWork.startAndGet(null);
        try {
            return Map.of("users", Stream.of("mihmax@gmail.com", "mmyk@ciklum.com", "test@gmail.com")
                                         .map((goods) -> AxonUtil.unwrap(userRepository.load(goods))).toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of(e.getMessage(), List.of());
        } finally {
            uow.rollback();
        }
    }
}
