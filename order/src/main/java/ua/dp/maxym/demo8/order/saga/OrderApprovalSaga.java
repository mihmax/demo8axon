package ua.dp.maxym.demo8.order.saga;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import ua.dp.maxym.demo8.inventory.aggregate.NotEnoughGoodsException;
import ua.dp.maxym.demo8.inventory.aggregate.GoodsAggregate;
import ua.dp.maxym.demo8.inventory.command.CancelPickingGoodsCommand;
import ua.dp.maxym.demo8.inventory.command.PickGoodsCommand;
import ua.dp.maxym.demo8.order.command.ApproveOrderCommand;
import ua.dp.maxym.demo8.order.command.RejectOrderCommand;
import ua.dp.maxym.demo8.order.event.OrderCreatedEvent;
import ua.dp.maxym.demo8.user.command.PayCommand;

import java.util.ArrayList;
import java.util.List;

@Saga
public class OrderApprovalSaga {

    @Autowired
    private CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderCreatedEvent event) {
        List<CancelPickingGoodsCommand> compensations = new ArrayList<>();

        boolean ok = true;
        String reason = null;

        // try to pick up all the goods
        for (GoodsAggregate goods : event.orderItems()) {
            try {
                commandGateway.sendAndWait(new PickGoodsCommand(goods.getName(), goods.getQuantity()));
                compensations.add(new CancelPickingGoodsCommand(goods.getName(), goods.getQuantity()));
            } catch (CommandExecutionException e) {
                if (e.getMessage().startsWith(NotEnoughGoodsException.class.getSimpleName())) {
                    // if picking up any of the goods fails, rolling back the successful ones
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
            commandGateway.send(new ApproveOrderCommand(event.orderId()));
        } else {
            commandGateway.send(new RejectOrderCommand(event.orderId(), reason));
        }

        SagaLifecycle.end();
    }
}
