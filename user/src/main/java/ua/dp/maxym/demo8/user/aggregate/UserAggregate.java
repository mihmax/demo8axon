package ua.dp.maxym.demo8.user.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import ua.dp.maxym.demo8.user.command.CreateUserCommand;
import ua.dp.maxym.demo8.user.command.PayCommand;
import ua.dp.maxym.demo8.user.event.ErrorChangingUserMoneyEvent;
import ua.dp.maxym.demo8.user.event.UserCreatedEvent;
import ua.dp.maxym.demo8.user.event.UserMoneyChangedEvent;

@Aggregate
public class UserAggregate {

    @AggregateIdentifier
    // private UUID id;
    public String email;
    private String firstName;
    private String lastName;
    private Double money;

    public UserAggregate() {
        System.out.print("UserAggregate default constructor called\n\n");
    }

    @CommandHandler
    public UserAggregate(CreateUserCommand createUserCommand) {
        System.out.printf("UserAggregate constructor called with %s\n", createUserCommand);
        AggregateLifecycle.apply(new UserCreatedEvent(createUserCommand.email(),
                                                      createUserCommand.firstName(),
                                                      createUserCommand.lastName(),
                                                      createUserCommand.money()));
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

    @CommandHandler
    public void handle(PayCommand command) {
        if (command.money() > getMoney()) {
            AggregateLifecycle.apply(new ErrorChangingUserMoneyEvent(getEmail(), command.money(), getMoney()));
        } else {
            AggregateLifecycle.apply(new UserMoneyChangedEvent(getEmail(), getMoney() - command.money()));
        }
    }

    @EventSourcingHandler
    public void on(UserCreatedEvent event) {
        System.out.printf("UserAggregate.on(UserCreatedEvent) called with %s\n\n", event);
        // this.id = UUID.randomUUID();
        this.email = event.email();
        this.firstName = event.firstName();
        this.lastName = event.lastName();
        this.money = event.money();
    }

    @EventSourcingHandler
    public void on(UserMoneyChangedEvent event) {
        this.money = event.newMoney();
    }
}
