package ua.dp.maxym.demo8.user.aggregate;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(String message, Object... args) {
        super(String.format(message, args));
    }
}
