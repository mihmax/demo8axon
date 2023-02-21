package ua.dp.maxym.demo8.inventory.aggregate;


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
import java.util.UUID;

@Aggregate
public class WarehouseAggregate {
    @AggregateIdentifier
    private String name;
    @AggregateMember
    private Map<String, SKU> skuMap;
    private Map<String, List<SKU>> reservations;

    public WarehouseAggregate() {
    }

    @CommandHandler
    public WarehouseAggregate(CreateWarehouseCommand command) {
        AggregateLifecycle.apply(new WarehouseCreatedEvent(command.name()));
    }

    @CommandHandler
    public void handle(AddSKUCommand command) {
        if (skuMap.containsKey(command.name())) {
            AggregateLifecycle.apply(new SKUArrivedEvent(command.name(), command.quantity()));
        } else {
            AggregateLifecycle.apply(new SKUCreatedEvent(command.name(), command.quantity(), command.pricePerItem()));
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
            String reservationId = UUID.randomUUID().toString();
            double reservationPrice = 0.0;
            for (Map.Entry<String, Integer> entry : command.skuMap().entrySet()) {
                SKU sku = skuMap.get(entry.getKey());
                reservationPrice += entry.getValue() * sku.getPricePerItem();
                AggregateLifecycle.apply(
                        new SKUQuantityChangedEvent(entry.getKey(),
                                                    sku.getQuantity() - entry.getValue()));
            }
            AggregateLifecycle.apply(new ReservationCreatedEvent(reservationId, command.skuMap(), reservationPrice));
        } else {
            AggregateLifecycle.apply(new ErrorCannotReserveSKUsEvent());
        }
    }

    @CommandHandler
    public void handle(ConfirmSKUsReservationCommand command) {
        if (reservations.containsKey(command.reservationId())) {
            AggregateLifecycle.apply(new ReservationConfirmedEvent(command.reservationId()));
        } else {
            AggregateLifecycle.apply(new ErrorReservationDoesNotExistEvent(command.reservationId()));
        }
    }

    @CommandHandler
    public void handle(CancelSKUsReservationCommand command) {
        if (reservations.containsKey(command.reservationId())) {
            AggregateLifecycle.apply(new ReservationCancelledEvent(command.reservationId()));
            reservations.get(command.reservationId()).stream()
                        .forEach(item -> AggregateLifecycle.apply(
                                new SKUQuantityChangedEvent(item.getName(),
                                                            item.getQuantity()
                                                                    + skuMap.get(item.getName()).getQuantity())));

        } else {
            AggregateLifecycle.apply(new ErrorReservationDoesNotExistEvent(command.reservationId()));
        }
    }

    public String getName() {
        return name;
    }

    public Map<String, SKU> getSkuMap() {
        return skuMap;
    }

    @EventSourcingHandler
    public void on(WarehouseCreatedEvent event) {
        this.name = event.name();
        this.skuMap = new HashMap<>();
    }

    @EventSourcingHandler
    public void on(SKUCreatedEvent event) {
        skuMap.put(event.name(), new SKU(event.name(), event.quantity(), event.pricePerItem()));
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
