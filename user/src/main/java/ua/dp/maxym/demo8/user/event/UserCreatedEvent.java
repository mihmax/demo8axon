package ua.dp.maxym.demo8.user.event;

public record UserCreatedEvent(String email, String firstName, String lastName, Double money) {
}
