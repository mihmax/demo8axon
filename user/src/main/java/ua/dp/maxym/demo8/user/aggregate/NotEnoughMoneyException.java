package ua.dp.maxym.demo8.user.aggregate;

import ua.dp.maxym.demo8.common.exception.Demo8Exception;

public class NotEnoughMoneyException extends Demo8Exception {
    public NotEnoughMoneyException(String message, Object... args) {
        super(String.format(message, args));
    }
}
