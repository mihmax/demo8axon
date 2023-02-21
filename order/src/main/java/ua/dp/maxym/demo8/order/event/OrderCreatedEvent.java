package ua.dp.maxym.demo8.order.event;

import java.util.Map;

public record OrderCreatedEvent(
        Integer orderId,
        String userId,
        Map<String, Integer> orderItems) {
}
