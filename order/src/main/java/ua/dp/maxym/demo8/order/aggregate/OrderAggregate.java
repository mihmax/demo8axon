package ua.dp.maxym.demo8.order.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import ua.dp.maxym.demo8.inventory.aggregate.GoodsAggregate;
import ua.dp.maxym.demo8.order.command.ApproveOrderCommand;
import ua.dp.maxym.demo8.order.command.CreateOrderCommand;
import ua.dp.maxym.demo8.order.command.RejectOrderCommand;
import ua.dp.maxym.demo8.order.event.OrderApprovedEvent;
import ua.dp.maxym.demo8.order.event.OrderCreatedEvent;
import ua.dp.maxym.demo8.order.event.OrderRejectedEvent;
import ua.dp.maxym.demo8.user.aggregate.UserAggregate;

import java.util.List;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private Integer number;
    private UserAggregate user;
    private List<GoodsAggregate> orderItems;
    private Double total;
    private OrderState state;
    private String rejectionReason;

    public OrderAggregate() {
        System.out.print("OrderAggregate default constructor called\n\n");
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        System.out.printf("Received CreateOrderCommand %s\n", command);
        var total = command.orderItems().stream().mapToDouble((agg) -> agg.getQuantity() * agg.getPricePerItem()).sum();
        AggregateLifecycle.apply(new OrderCreatedEvent(command.n(), command.user(), command.orderItems(), total));
    }

    public OrderState getState() {
        return state;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public Integer getNumber() {
        return number;
    }

    public UserAggregate getUser() {
        return user;
    }

    public List<GoodsAggregate> getOrderItems() {
        return orderItems;
    }

    public Double getTotal() {
        return total;
    }

    @CommandHandler
    public void handle(ApproveOrderCommand command) {
        AggregateLifecycle.apply(new OrderApprovedEvent());
    }

    @CommandHandler
    public void handle(RejectOrderCommand command) {
        AggregateLifecycle.apply(new OrderRejectedEvent());
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        System.out.printf("OrderAggregate.on(OrderCreatedEvent) called with %s\n\n", event);
        this.number = event.orderId();
        this.user = event.user();
        this.orderItems = event.orderItems();
        this.total = event.total();
        this.state = OrderState.PENDING;
        this.rejectionReason = null;
    }
}
