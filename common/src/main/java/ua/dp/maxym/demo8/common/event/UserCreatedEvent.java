package ua.dp.maxym.demo8.common.event;

public record UserCreatedEvent(String email, String firstName, String lastName, Double money) {
}
