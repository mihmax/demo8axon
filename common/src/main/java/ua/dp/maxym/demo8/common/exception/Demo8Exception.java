package ua.dp.maxym.demo8.common.exception;

public class Demo8Exception extends Exception {
    private final String message;

    public Demo8Exception(String message, Object... args) {
        super();
        this.message = String.format(getClass().getSimpleName() + ": " + message, args);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
