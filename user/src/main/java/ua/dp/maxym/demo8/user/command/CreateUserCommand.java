package ua.dp.maxym.demo8.user.command;

public record CreateUserCommand(String email, String firstName, String lastName, Double money) {
}
