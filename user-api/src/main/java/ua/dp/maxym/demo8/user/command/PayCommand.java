package ua.dp.maxym.demo8.user.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record PayCommand(@TargetAggregateIdentifier String userEmail, String paymentId, Double money) {
}
