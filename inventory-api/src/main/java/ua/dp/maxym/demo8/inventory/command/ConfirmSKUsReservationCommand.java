package ua.dp.maxym.demo8.inventory.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record ConfirmSKUsReservationCommand(@TargetAggregateIdentifier String warehouseId, String reservationId) {
}
