package ua.dp.maxym.demo8.inventory.event;

public record ErrorReservationFailedEvent(String warehouseId, String reservationId) {
}
