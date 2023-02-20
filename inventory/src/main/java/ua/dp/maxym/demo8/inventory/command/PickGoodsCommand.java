package ua.dp.maxym.demo8.inventory.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record PickGoodsCommand(@TargetAggregateIdentifier String name, Integer quantity) {
}
