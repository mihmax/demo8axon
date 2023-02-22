package ua.dp.maxym.demo8.user.event;

public record UserMoneyChangedEvent(String userId, String paymentId, Double newMoney) {
}
