package ua.dp.maxym.demo8.inventory.event;

public record SKUQuantityChangedEvent(String warehouseId, String skuName, Integer newQuantity) {
}
