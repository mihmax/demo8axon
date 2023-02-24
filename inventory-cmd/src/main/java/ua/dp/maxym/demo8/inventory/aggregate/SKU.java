package ua.dp.maxym.demo8.inventory.aggregate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.EntityId;
import ua.dp.maxym.demo8.inventory.event.SKUQuantityChangedEvent;

import java.util.Objects;

@Getter @EqualsAndHashCode @ToString
public class SKU {

    @EntityId(routingKey = "skuName")
    private final String skuName;
    private final Double pricePerItem;
    private Integer quantity;

    public SKU(String skuName, Integer quantity, Double pricePerItem) {
        this.skuName = skuName;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
    }

    @EventSourcingHandler
    public void on(SKUQuantityChangedEvent event) {
        // Axon is sending same events to all members of collection
        // And @EntityId(routingKey = "skuName") does not work
        // It works for Command Handlers, but not for Event Handlers :(
        if (Objects.equals(getSkuName(), event.skuName())) {
            this.quantity = event.newQuantity();
        }
    }
}
