package ua.dp.maxym.demo8.inventory.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CreateGoodsCommand(@TargetAggregateIdentifier String name, Integer quantity, Double pricePerItem) {
}
