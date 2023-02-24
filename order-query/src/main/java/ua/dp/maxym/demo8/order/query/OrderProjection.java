package ua.dp.maxym.demo8.order.query;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import ua.dp.maxym.demo8.order.event.OrderApprovedEvent;
import ua.dp.maxym.demo8.order.event.OrderCreatedEvent;
import ua.dp.maxym.demo8.order.event.OrderRejectedEvent;
import ua.dp.maxym.demo8.order.event.OrderTotalChangedEvent;

@Component
public class OrderProjection {

    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;

    public OrderProjection(OrderRepository orderRepo, OrderItemRepository itemRepo) {
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        Order order = orderRepo.save(new Order(event.orderId(), event.userId(), 0.0, "new", ""));
        event.orderItems().forEach((name, quantity) -> {
            OrderItem item = itemRepo.save(new OrderItem(order, name, quantity));
            order.getOrderItems().add(item);
        });
        orderRepo.save(order);
    }

    @EventHandler
    public void on(OrderTotalChangedEvent event) {
        Order order = orderRepo.findByOrderId(event.orderId());
        order.setTotal(event.newTotal());
        orderRepo.save(order);
    }

    @EventHandler
    public void on(OrderApprovedEvent event) {
        Order order = orderRepo.findByOrderId(event.orderId());
        order.setState("approved");
        orderRepo.save(order);
    }

    @EventHandler
    public void on(OrderRejectedEvent event) {
        Order order = orderRepo.findByOrderId(event.orderId());
        order.setState("rejected");
        order.setRejectionReason(event.rejectionReason());
        orderRepo.save(order);
    }

}
