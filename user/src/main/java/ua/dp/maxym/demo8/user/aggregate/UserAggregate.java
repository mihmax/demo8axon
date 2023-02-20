package ua.dp.maxym.demo8.user.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import ua.dp.maxym.demo8.common.event.UserCreatedEvent;
import ua.dp.maxym.demo8.common.exception.DuplicateEmailException;
import ua.dp.maxym.demo8.user.command.CreateUserCommand;

import java.util.UUID;

@Aggregate
public class UserAggregate {

    @AggregateIdentifier
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Double money;

    public UserAggregate() {
        System.out.print("UserAggregate default constructor called\n\n");
    }

    @CommandHandler
    public UserAggregate(CreateUserCommand createUserCommand, EmailRepository emailRepository) {
        System.out.printf("UserAggregate constructor called with %s\n", createUserCommand);
        System.out.printf("Checking if email %s exists\n", createUserCommand.email());
        if (emailRepository.existsById(createUserCommand.email())) {
            System.out.println("Yeap :(");
            throw new DuplicateEmailException("Account with email address %s already exists",
                                              createUserCommand.email());
        }
        System.out.println("Nope :)");
        AggregateLifecycle.apply(new UserCreatedEvent(createUserCommand.email(),
                                                      createUserCommand.firstName(),
                                                      createUserCommand.lastName(),
                                                      createUserCommand.money()));
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Double getMoney() {
        return money;
    }

    @EventSourcingHandler
    public void on(UserCreatedEvent event) {
        System.out.printf("UserAggregate.on(UserCreatedEvent) called with %s\n\n", event);
        this.id = UUID.randomUUID();
        this.email = event.email();
        this.firstName = event.firstName();
        this.lastName = event.lastName();
        this.money = event.money();
    }

}
