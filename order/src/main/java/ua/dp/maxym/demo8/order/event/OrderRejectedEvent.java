package ua.dp.maxym.demo8.order.event;

public record OrderRejectedEvent(Integer orderId, String rejectionReason) {
}
