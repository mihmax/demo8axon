package ua.dp.maxym.demo8.order.command;

import java.util.Map;

public record CreateOrderCommand(
        String userId,
        Map<String, Integer> orderItems) {
}
