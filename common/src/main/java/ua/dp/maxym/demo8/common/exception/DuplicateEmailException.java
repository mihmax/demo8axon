package ua.dp.maxym.demo8.common.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message, Object... args) {
        super(String.format(message, args));
    }
}
