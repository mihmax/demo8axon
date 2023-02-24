package ua.dp.maxym.demo8.user.aggregate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import ua.dp.maxym.demo8.user.command.CreateUserCommand;
import ua.dp.maxym.demo8.user.command.PayCommand;
import ua.dp.maxym.demo8.user.event.ErrorNotEnoughMoneyEvent;
import ua.dp.maxym.demo8.user.event.UserCreatedEvent;
import ua.dp.maxym.demo8.user.event.UserMoneyChangedEvent;

@Aggregate
@Getter @EqualsAndHashCode @ToString
public class UserAggregate {

    @AggregateIdentifier
    // private UUID id;
    public String email;
    private String firstName;
    private String lastName;
    private Double money;

    public UserAggregate() {
    }

    @CommandHandler
    public UserAggregate(CreateUserCommand createUserCommand) {
        AggregateLifecycle.apply(new UserCreatedEvent(createUserCommand.email(),
                                                      createUserCommand.firstName(),
                                                      createUserCommand.lastName(),
                                                      createUserCommand.money()));
    }

    @CommandHandler
    public void handle(PayCommand command) {
        if (command.money() > getMoney()) {
            AggregateLifecycle.apply(new ErrorNotEnoughMoneyEvent(getEmail(), command.paymentId(), command.money(), getMoney()));
        } else {
            AggregateLifecycle.apply(new UserMoneyChangedEvent(getEmail(), command.paymentId(), getMoney() - command.money()));
        }
    }

    @EventSourcingHandler
    public void on(UserCreatedEvent event) {
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
