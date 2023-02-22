package ua.dp.maxym.demo8.order.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record UpdateOrderTotalCommand(@TargetAggregateIdentifier String orderId, Double newTotal) {
}
