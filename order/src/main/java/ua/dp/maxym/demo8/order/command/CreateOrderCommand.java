package ua.dp.maxym.demo8.order.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import ua.dp.maxym.demo8.inventory.aggregate.GoodsAggregate;
import ua.dp.maxym.demo8.user.aggregate.UserAggregate;

import java.util.List;

public record CreateOrderCommand(@TargetAggregateIdentifier Integer orderId, UserAggregate user, List<GoodsAggregate> orderItems) {
}
