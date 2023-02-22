package ua.dp.maxym.demo8.user.event;

public record ErrorNotEnoughMoneyEvent(String userId, String paymentId, Double requestedMoney, Double availableMoney) {
}
