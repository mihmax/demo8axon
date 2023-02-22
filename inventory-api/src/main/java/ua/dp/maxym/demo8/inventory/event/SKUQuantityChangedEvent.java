package ua.dp.maxym.demo8.inventory.event;

public record SKUQuantityChangedEvent(String skuName, Integer newQuantity) {
}
