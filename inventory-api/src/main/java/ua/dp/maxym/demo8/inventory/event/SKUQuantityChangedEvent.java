package ua.dp.maxym.demo8.inventory.event;

public record SKUQuantityChangedEvent(String name, Integer newQuantity) {
}
