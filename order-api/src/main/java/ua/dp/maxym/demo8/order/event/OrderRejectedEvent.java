package ua.dp.maxym.demo8.order.event;

public record OrderRejectedEvent(String orderId, String rejectionReason) {
}
