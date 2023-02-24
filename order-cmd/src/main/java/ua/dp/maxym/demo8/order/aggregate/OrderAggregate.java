package ua.dp.maxym.demo8.order.aggregate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import ua.dp.maxym.demo8.order.command.ApproveOrderCommand;
import ua.dp.maxym.demo8.order.command.CreateOrderCommand;
import ua.dp.maxym.demo8.order.command.RejectOrderCommand;
import ua.dp.maxym.demo8.order.command.UpdateOrderTotalCommand;
import ua.dp.maxym.demo8.order.event.OrderApprovedEvent;
import ua.dp.maxym.demo8.order.event.OrderCreatedEvent;
import ua.dp.maxym.demo8.order.event.OrderRejectedEvent;
import ua.dp.maxym.demo8.order.event.OrderTotalChangedEvent;

import java.util.Map;
import java.util.UUID;

@Aggregate
@Getter @EqualsAndHashCode @ToString
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String userId;
    private Map<String, Integer> orderItems;
    private Double total;
    private OrderState state;
    private String rejectionReason;

    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        String orderId = UUID.randomUUID().toString();
        AggregateLifecycle.apply(new OrderCreatedEvent(orderId, command.userId(), command.orderItems()));
    }

    @CommandHandler
    public void handle(ApproveOrderCommand command) {
        AggregateLifecycle.apply(new OrderApprovedEvent(orderId));
    }

    @CommandHandler
    public void handle(UpdateOrderTotalCommand command) {
        AggregateLifecycle.apply(new OrderTotalChangedEvent(orderId, command.newTotal()));
    }

    @CommandHandler
    public void handle(RejectOrderCommand command) {
        AggregateLifecycle.apply(new OrderRejectedEvent(orderId, command.reason()));
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.orderId();
        this.userId = event.userId();
        this.orderItems = event.orderItems();
        this.total = null; // unknown yet
        this.state = OrderState.PENDING;
        this.rejectionReason = null;
    }

    @EventSourcingHandler
    public void on(OrderTotalChangedEvent event) {
        this.total = event.newTotal();
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent event) {
        this.state = OrderState.APPROVED;
    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent event) {
        this.state = OrderState.REJECTED;
        this.rejectionReason = event.rejectionReason();
    }
}
