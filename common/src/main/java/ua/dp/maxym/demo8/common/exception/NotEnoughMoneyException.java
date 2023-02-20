package ua.dp.maxym.demo8.common.exception;

public class NotEnoughMoneyException extends Demo8Exception {
    public NotEnoughMoneyException(String message, Object... args) {
        super(String.format(message, args));
    }
}
