package ua.dp.maxym.demo8.inventory.event;

public record GoodsCreatedEvent(String name, Integer quantity, Double pricePerItem) {
}
