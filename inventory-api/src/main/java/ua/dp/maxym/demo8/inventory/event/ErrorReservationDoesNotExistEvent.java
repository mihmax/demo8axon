package ua.dp.maxym.demo8.inventory.event;

public record ErrorReservationDoesNotExistEvent(String warehouseId, String reservationId) {
}
