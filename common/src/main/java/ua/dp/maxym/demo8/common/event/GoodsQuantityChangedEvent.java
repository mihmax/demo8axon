package ua.dp.maxym.demo8.common.event;

public record GoodsQuantityChangedEvent(String name, Integer oldQuantity, Integer newQuantity) {
}
