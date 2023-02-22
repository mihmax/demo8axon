package ua.dp.maxym.demo8.inventory.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Map;

/**
 * @param warehouseId
 * @param skuMap       Map from SKU name to requested quantity,
 *                    e.g. {"Item1": 1, "Item2": 2} means we're requesting 1 of Item1 and 2 of Item2
 */
public record ReserveSKUsCommand(@TargetAggregateIdentifier String warehouseId, String reservationId, Map<String, Integer> skuMap) {
}
