package ua.dp.maxym.demo8.order.event;

import ua.dp.maxym.demo8.inventory.aggregate.GoodsAggregate;
import ua.dp.maxym.demo8.user.aggregate.UserAggregate;

import java.util.List;

public record OrderCreatedEvent(Integer orderId, UserAggregate user, List<GoodsAggregate> orderItems, Double total) {
}
