package ua.dp.maxym.demo8.order.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Map;

public record CreateOrderCommand(
        @TargetAggregateIdentifier String orderId,
        String userId,
        Map<String, Integer> orderItems) {
}
