package ua.dp.maxym.demo8.order.command;

import ua.dp.maxym.demo8.inventory.aggregate.GoodsAggregate;
import ua.dp.maxym.demo8.user.aggregate.UserAggregate;

import java.util.List;

public record CreateOrderCommand(int n, UserAggregate user, List<GoodsAggregate> orderItems) {
}
