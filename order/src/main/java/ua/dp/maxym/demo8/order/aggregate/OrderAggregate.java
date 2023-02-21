package ua.dp.maxym.demo8.order.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import ua.dp.maxym.demo8.order.command.ApproveOrderCommand;
import ua.dp.maxym.demo8.order.command.CreateOrderCommand;
import ua.dp.maxym.demo8.order.command.RejectOrderCommand;
import ua.dp.maxym.demo8.order.event.OrderApprovedEvent;
import ua.dp.maxym.demo8.order.event.OrderCreatedEvent;
import ua.dp.maxym.demo8.order.event.OrderRejectedEvent;

import java.util.Map;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private Integer orderId;
    private String userId;
    private Map<String, Integer> orderItems;
    private Double total;
    private OrderState state;
    private String rejectionReason;

    public OrderAggregate() {
        System.out.print("OrderAggregate default constructor called\n\n");
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        System.out.printf("Received CreateOrderCommand %s\n", command);
        AggregateLifecycle.apply(new OrderCreatedEvent(command.orderId(), command.userId(), command.orderItems()));
    }

    public OrderState getState() {
        return state;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, Integer> getOrderItems() {
        return orderItems;
    }

    public Double getTotal() {
        return total;
    }

    @CommandHandler
    public void handle(ApproveOrderCommand command) {
        AggregateLifecycle.apply(new OrderApprovedEvent(command.total()));
    }

    @CommandHandler
    public void handle(RejectOrderCommand command) {
        AggregateLifecycle.apply(new OrderRejectedEvent(command.reason()));
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        System.out.printf("OrderAggregate.on(OrderCreatedEvent) called with %s\n\n", event);
        this.orderId = event.orderId();
        this.userId = event.userId();
        this.orderItems = event.orderItems();
        this.total = null; // unknown yet
        this.state = OrderState.PENDING;
        this.rejectionReason = null;
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent event) {
        this.total = event.total();
        this.state = OrderState.APPROVED;
    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent event) {
        this.state = OrderState.REJECTED;
        this.rejectionReason = event.rejectionReason();
    }
}
