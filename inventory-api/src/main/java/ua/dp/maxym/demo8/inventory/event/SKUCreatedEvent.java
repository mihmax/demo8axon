package ua.dp.maxym.demo8.inventory.event;

public record SKUCreatedEvent(String name, Integer quantity, Double pricePerItem) {
}
