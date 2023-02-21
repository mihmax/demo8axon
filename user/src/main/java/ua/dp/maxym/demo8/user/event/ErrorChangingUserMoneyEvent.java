package ua.dp.maxym.demo8.user.event;

public record ErrorChangingUserMoneyEvent(String userId, Double requestedMoney, Double availableMoney) {
}
