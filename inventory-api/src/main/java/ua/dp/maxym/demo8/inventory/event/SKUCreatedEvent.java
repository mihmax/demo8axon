package ua.dp.maxym.demo8.inventory.event;

public record SKUCreatedEvent(String warehouseId, String skuName, Integer quantity, Double pricePerItem) {
}
