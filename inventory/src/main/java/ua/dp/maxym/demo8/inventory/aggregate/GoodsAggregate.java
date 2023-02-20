package ua.dp.maxym.demo8.inventory.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import ua.dp.maxym.demo8.inventory.command.CancelPickingGoodsCommand;
import ua.dp.maxym.demo8.inventory.command.CreateGoodsCommand;
import ua.dp.maxym.demo8.inventory.command.PickGoodsCommand;
import ua.dp.maxym.demo8.inventory.event.GoodsCreatedEvent;
import ua.dp.maxym.demo8.inventory.event.GoodsQuantityChangedEvent;

@Aggregate
public class GoodsAggregate {

    @AggregateIdentifier
    public String name;
    public Integer quantity;
    public Double pricePerItem;

    public GoodsAggregate() {
        System.out.print("GoodsAggregate default constructor called\n\n");
    }

    @CommandHandler
    public GoodsAggregate(CreateGoodsCommand command) {
        System.out.printf("Received CreateGoodsCommand %s\n", command);
        AggregateLifecycle.apply(new GoodsCreatedEvent(command.name(), command.quantity(),
                                                       command.pricePerItem()));
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPricePerItem() {
        return pricePerItem;
    }

    @CommandHandler
    public void handle(PickGoodsCommand command) throws NotEnoughGoodsException {
        if (quantity < command.quantity()) {
            throw new NotEnoughGoodsException("Not enough %s (requested %s while there's only %s in stock", name,
                                              command.quantity(), quantity);
        }
        AggregateLifecycle.apply(new GoodsQuantityChangedEvent(name, quantity - command.quantity()));
    }

    @CommandHandler
    public void handle(CancelPickingGoodsCommand command) {
        AggregateLifecycle.apply(new GoodsQuantityChangedEvent(name, quantity + command.quantity()));
    }

    @EventSourcingHandler
    public void on(GoodsCreatedEvent goodsCreatedEvent) {
        System.out.printf("GoodsAggregate.on(GoodsCreatedEvent) called with %s\n\n", goodsCreatedEvent);
        this.name = goodsCreatedEvent.name();
        this.quantity = goodsCreatedEvent.quantity();
        this.pricePerItem = goodsCreatedEvent.pricePerItem();
    }

    @EventSourcingHandler
    public void on(GoodsQuantityChangedEvent goodsQuantityChangedEvent) {
        System.out.printf("GoodsAggregate.on(GoodsQuantityChangedEvent) called with %s\n\n", goodsQuantityChangedEvent);
        quantity = goodsQuantityChangedEvent.newQuantity();
    }
}
