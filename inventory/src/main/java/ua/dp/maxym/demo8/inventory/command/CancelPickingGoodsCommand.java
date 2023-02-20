package ua.dp.maxym.demo8.inventory.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CancelPickingGoodsCommand(@TargetAggregateIdentifier String name, Integer quantity) {
}
