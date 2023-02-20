package ua.dp.maxym.demo8.inventory.command;

public record CreateGoodsCommand(String name, Integer quantity, Double pricePerItem) {
}
