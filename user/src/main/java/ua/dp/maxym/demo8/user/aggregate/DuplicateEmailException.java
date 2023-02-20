package ua.dp.maxym.demo8.user.aggregate;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message, Object... args) {
        super(String.format(message, args));
    }
}
