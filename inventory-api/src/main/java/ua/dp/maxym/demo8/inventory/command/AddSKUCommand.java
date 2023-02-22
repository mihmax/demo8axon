package ua.dp.maxym.demo8.inventory.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record AddSKUCommand(@TargetAggregateIdentifier String warehouseId, String skuName, Integer quantity, Double pricePerItem) {
}
