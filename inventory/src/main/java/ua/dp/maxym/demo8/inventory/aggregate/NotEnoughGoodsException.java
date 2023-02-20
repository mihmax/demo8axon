package ua.dp.maxym.demo8.inventory.aggregate;

public class NotEnoughGoodsException extends RuntimeException {
    public NotEnoughGoodsException(String message, Object... args) {
        super(String.format(message, args));
    }
}
