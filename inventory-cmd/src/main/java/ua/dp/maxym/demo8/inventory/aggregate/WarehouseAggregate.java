package ua.dp.maxym.demo8.inventory.aggregate;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;
import ua.dp.maxym.demo8.inventory.command.*;
import ua.dp.maxym.demo8.inventory.event.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aggregate
@Getter @EqualsAndHashCode @ToString
public class WarehouseAggregate {
    private final Map<String, List<SKU>> reservations = new HashMap<>();
    @AggregateMember
    private final Map<String, SKU> skuMap = new HashMap<>();
    @AggregateIdentifier
    private String warehouseId;

    public WarehouseAggregate() {
    }

    @CommandHandler
    public WarehouseAggregate(CreateWarehouseCommand command) {
        AggregateLifecycle.apply(new WarehouseCreatedEvent(command.warehouseId()));
    }

    @CommandHandler
    public void handle(AddSKUCommand command) {
        if (skuMap.containsKey(command.skuName())) {
            var newQuantity = command.quantity() + skuMap.get(command.skuName()).getQuantity();
            AggregateLifecycle.apply(new SKUQuantityChangedEvent(warehouseId, command.skuName(), newQuantity));
        } else {
            AggregateLifecycle.apply(
                    new SKUCreatedEvent(warehouseId, command.skuName(), command.quantity(), command.pricePerItem()));
        }
    }

    @CommandHandler
    public void handle(ReserveSKUsCommand command) {
        if (null == command.skuMap().entrySet().stream()
                           .filter(entry -> skuMap.containsKey(
                                   entry.getKey()) && skuMap.get(entry.getKey())
                                                            .getQuantity() < entry.getValue())
                           .findAny().orElse(null)) {
            // creating reservation
            double reservationPrice = 0.0;
            for (Map.Entry<String, Integer> entry : command.skuMap().entrySet()) {
                SKU sku = skuMap.get(entry.getKey());
                reservationPrice += entry.getValue() * sku.getPricePerItem();
                AggregateLifecycle.apply(
                        new SKUQuantityChangedEvent(warehouseId, entry.getKey(),
                                                    sku.getQuantity() - entry.getValue()));
            }
            AggregateLifecycle.apply(
                    new ReservationCreatedEvent(warehouseId, command.reservationId(), command.skuMap(),
                                                reservationPrice));
        } else {
            AggregateLifecycle.apply(new ErrorReservationFailedEvent(warehouseId, command.reservationId()));
        }
    }

    @CommandHandler
    public void handle(ConfirmSKUsReservationCommand command) {
        if (reservations.containsKey(command.reservationId())) {
            AggregateLifecycle.apply(new ReservationConfirmedEvent(warehouseId, command.reservationId()));
        } else {
            AggregateLifecycle.apply(new ErrorReservationDoesNotExistEvent(warehouseId, command.reservationId()));
        }
    }

    @CommandHandler
    public void handle(CancelSKUsReservationCommand command) {
        String reservationId = command.reservationId();
        if (reservations.containsKey(reservationId)) {
            reservations.get(reservationId)
                        .forEach(item -> AggregateLifecycle.apply(
                                new SKUQuantityChangedEvent(warehouseId, item.getSkuName(),
                                                            item.getQuantity()
                                                                    + skuMap.get(item.getSkuName()).getQuantity())));
            AggregateLifecycle.apply(new ReservationCancelledEvent(reservationId));
        } else {
            AggregateLifecycle.apply(new ErrorReservationDoesNotExistEvent(warehouseId, reservationId));
        }
    }

    @EventSourcingHandler
    public void on(WarehouseCreatedEvent event) {
        this.warehouseId = event.name();
    }

    @EventSourcingHandler
    public void on(SKUCreatedEvent event) {
        skuMap.put(event.skuName(), new SKU(event.skuName(), event.quantity(), event.pricePerItem()));
    }

    @EventSourcingHandler
    public void on(ReservationCreatedEvent event) {
        List<SKU> skuList = event.skuMap().entrySet().stream()
                                 .map(entry -> new SKU(entry.getKey(), entry.getValue(),
                                                       skuMap.get(entry.getKey()).getPricePerItem()))
                                 .toList();
        reservations.put(event.reservationId(), skuList);
    }

    @EventSourcingHandler
    public void on(ReservationConfirmedEvent event) {
        reservations.remove(event.reservationId());
    }

    @EventSourcingHandler
    public void on(ReservationCancelledEvent event) {
        reservations.remove(event.reservationId());
    }
}
