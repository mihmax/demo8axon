package ua.dp.maxym.demo8.inventory.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CancelSKUsReservationCommand(@TargetAggregateIdentifier String warehouseId, String reservationId) {
}
