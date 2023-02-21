package ua.dp.maxym.demo8.order.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.modelling.command.Repository;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import ua.dp.maxym.demo8.inventory.command.CancelSKUsReservationCommand;
import ua.dp.maxym.demo8.inventory.command.ConfirmSKUsReservationCommand;
import ua.dp.maxym.demo8.inventory.command.ReserveSKUsCommand;
import ua.dp.maxym.demo8.inventory.event.ErrorReservationDoesNotExistEvent;
import ua.dp.maxym.demo8.inventory.event.ErrorReservationFailedEvent;
import ua.dp.maxym.demo8.inventory.event.ReservationConfirmedEvent;
import ua.dp.maxym.demo8.inventory.event.ReservationCreatedEvent;
import ua.dp.maxym.demo8.order.aggregate.OrderAggregate;
import ua.dp.maxym.demo8.order.command.ApproveOrderCommand;
import ua.dp.maxym.demo8.order.command.RejectOrderCommand;
import ua.dp.maxym.demo8.order.event.OrderApprovedEvent;
import ua.dp.maxym.demo8.order.event.OrderCreatedEvent;
import ua.dp.maxym.demo8.order.event.OrderRejectedEvent;
import ua.dp.maxym.demo8.user.command.PayCommand;
import ua.dp.maxym.demo8.user.event.ErrorChangingUserMoneyEvent;
import ua.dp.maxym.demo8.user.event.UserMoneyChangedEvent;

import java.time.Duration;
import java.util.Map;

import static ua.dp.maxym.demo8.order.saga.SagaState.*;

@Saga
public class OrderApprovalSaga {

    private static final String DEADLINE_NAME = "orderApprovalDeadline";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    Repository<OrderAggregate> orderRepository;
    @Autowired
    private transient CommandGateway commandGateway;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private transient DeadlineManager deadlineManager;


    private String deadlineId;
    private Integer orderId;
    private String userId;
    private Map<String, Integer> orderItems;

    private SagaState state = null;
    private String reservationId;
    private Double orderTotal;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderCreatedEvent event) {
        if (state != null) throw new AssertionError("Saga should start here, but state's non-null");
        deadlineId = deadlineManager.schedule(Duration.ofMillis(50000), DEADLINE_NAME);

        orderId = event.orderId();
        userId = event.userId();
        orderItems = event.orderItems();

        SagaLifecycle.associateWith("warehouseId", "warehouse1");
        commandGateway.send(new ReserveSKUsCommand("warehouse1", event.orderItems()));

        state = RESERVING_SKUs;
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "warehouseId")
    public void on(ErrorReservationFailedEvent event) {
        if (state != RESERVING_SKUs) throw new AssertionError("Invalid Saga state");
        cancelEverything(event);
    }

    @SagaEventHandler(associationProperty = "warehouseId")
    public void on(ReservationCreatedEvent event) {
        if (state != RESERVING_SKUs) throw new AssertionError("Invalid Saga state");

        reservationId = event.reservationId();
        orderTotal = event.reservationPrice();

        SagaLifecycle.associateWith("userId", userId);
        commandGateway.sendAndWait(new PayCommand(userId, orderTotal));

        state = PAYING;
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "userId")
    public void on(ErrorChangingUserMoneyEvent event) {
        if (state != PAYING) throw new AssertionError("Invalid Saga state");
        cancelEverything(event);
    }

    @SagaEventHandler(associationProperty = "userId")
    public void on(UserMoneyChangedEvent event) {
        if (state != PAYING) throw new AssertionError("Invalid Saga state");

        commandGateway.send(new ConfirmSKUsReservationCommand(reservationId));

        state = CONFIRMING_SKU;
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "warehouseId")
    public void on(ErrorReservationDoesNotExistEvent event) {
        if (state != CONFIRMING_SKU) throw new AssertionError("Invalid Saga state");
        cancelEverything(event);
    }

    @SagaEventHandler(associationProperty = "warehouseId")
    public void on(ReservationConfirmedEvent event) {
        if (state != CONFIRMING_SKU) throw new AssertionError("Invalid Saga state");

        commandGateway.send(new ApproveOrderCommand(orderTotal));

        state = CONFIRMING_ORDER;
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderApprovedEvent event) {
        if (state != CONFIRMING_ORDER) throw new AssertionError("Invalid Saga state");

        deadlineManager.cancelSchedule(DEADLINE_NAME, deadlineId);

        state = SUCCEEDED;
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderRejectedEvent event) {
        if (state != REJECTING_ORDER) throw new AssertionError("Invalid Saga state");

        deadlineManager.cancelSchedule(DEADLINE_NAME, deadlineId);

        state = FAILED;
    }

    @EndSaga
    @DeadlineHandler(deadlineName = DEADLINE_NAME)
    public void onDeadline() {
        cancelEverything(deadlineManager);
    }

    private void cancelEverything(Object event) {
        switch (state) {
            case PAYING:
            case RESERVING_SKUs:
            case CONFIRMING_SKU:
            case CONFIRMING_ORDER:
                commandGateway.send(new CancelSKUsReservationCommand("warehouse1", reservationId));
                commandGateway.send(new RejectOrderCommand(event.getClass().getSimpleName()));
                break;
            default:
                throw new AssertionError("Invalid Saga state");
        }
    }

/*
        // try to pick up all the skuList
        for (GoodsAggregate goods : event.orderItems()) {
            try {
                commandGateway.sendAndWait(new ReserveSKUsCommand(goods.getName(), goods.getQuantity()));
                compensations.add(new CancelSKUsReservationCommand(goods.getName(), goods.getQuantity()));
            } catch (CommandExecutionException e) {
                if (e.getMessage().startsWith(NotEnoughGoodsException.class.getSimpleName())) {
                    // if picking up any of the skuList fails, rolling back the successful ones
                    compensations.forEach(compensation -> commandGateway.send(compensation));
                    ok = false;
                    reason = e.getMessage();
                    break;
                } else {
                    System.out.println("ERROR. Something unexpected happened!");
                    throw e;
                }
            }
        }

        // try to pay
        if (ok) {
            double money = event.orderItems().stream()
                                .mapToDouble(goods -> goods.getQuantity() * goods.getPricePerItem()).sum();
            try {
                commandGateway.sendAndWait(new PayCommand(event.user().getEmail(), money));
            } catch (Exception e) {
                compensations.forEach(compensation -> commandGateway.send(compensation));
                ok = false;
                reason = e.getMessage();
            }
        }

        if (ok) {
            try {
                orderRepository.load(event.orderId().toString()).handle(
                        GenericCommandMessage.asCommandMessage(new ApproveOrderCommand()));
            } catch (Exception e) {
                System.out.println("ERRRRRRRROR");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            try {
                orderRepository.load(event.orderId().toString()).handle(
                        GenericCommandMessage.asCommandMessage(new RejectOrderCommand(reason)));
            } catch (Exception e) {
                System.out.println("ERRRRRRRROR");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        SagaLifecycle.end();
    }

 */

}
