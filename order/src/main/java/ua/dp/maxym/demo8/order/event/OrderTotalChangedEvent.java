package ua.dp.maxym.demo8.order.event;

public record OrderTotalChangedEvent(String orderId, Double newTotal) {
}
