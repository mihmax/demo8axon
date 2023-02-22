package ua.dp.maxym.demo8.inventory.aggregate;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.EntityId;
import ua.dp.maxym.demo8.inventory.event.SKUQuantityChangedEvent;

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

    public String getSkuName() {
        return skuName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPricePerItem() {
        return pricePerItem;
    }

    @EventSourcingHandler
    public void on(SKUQuantityChangedEvent event) {
        // Axon is sending same events to all members of collection
        // And @EntityId(routingKey = "skuName") does not work
        // It works for Command Handlers, but not for Event Handlers :(
        if (this.skuName.equals(event.skuName())) {
            this.quantity = event.newQuantity();
        } else {
            // Looks like this does not work in event handlers
            System.out.println("@EntityId should do the trick, right?");
        }
    }
}
