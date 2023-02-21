package ua.dp.maxym.demo8.inventory.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import ua.dp.maxym.demo8.inventory.event.SKUQuantityChangedEvent;

public class SKU {

    private String name;
    private Integer quantity;
    private Double pricePerItem;

    public SKU(String name, Integer quantity, Double pricePerItem) {
        this.name = name;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
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

    @EventSourcingHandler
    public void on(SKUQuantityChangedEvent command) {
        this.quantity = command.newQuantity();
    }
}
