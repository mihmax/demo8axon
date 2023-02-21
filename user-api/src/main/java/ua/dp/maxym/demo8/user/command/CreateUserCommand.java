package ua.dp.maxym.demo8.user.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CreateUserCommand(@TargetAggregateIdentifier String email, String firstName, String lastName, Double money) {
}
