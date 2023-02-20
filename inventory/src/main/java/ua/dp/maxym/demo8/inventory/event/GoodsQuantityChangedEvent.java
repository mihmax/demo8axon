package ua.dp.maxym.demo8.inventory.event;

public record GoodsQuantityChangedEvent(String name, Integer oldQuantity, Integer newQuantity) {
}
