package ua.dp.maxym.demo8.user.event;

public record ErrorNotEnoughMoneyEvent(String userId, Double requestedMoney, Double availableMoney) {
}
