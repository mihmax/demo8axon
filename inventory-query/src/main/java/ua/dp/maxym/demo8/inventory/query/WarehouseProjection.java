package ua.dp.maxym.demo8.inventory.query;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import ua.dp.maxym.demo8.inventory.event.SKUCreatedEvent;
import ua.dp.maxym.demo8.inventory.event.SKUQuantityChangedEvent;
import ua.dp.maxym.demo8.inventory.event.WarehouseCreatedEvent;

@Component
public class WarehouseProjection {

    private final WarehouseRepository warehouseRepo;
    private final SKURepository skuRepo;

    public WarehouseProjection(WarehouseRepository warehouseRepo, SKURepository skuRepo) {
        this.warehouseRepo = warehouseRepo;
        this.skuRepo = skuRepo;
    }

    @EventHandler
    public void on(WarehouseCreatedEvent event) {
        warehouseRepo.save(new Warehouse(event.name()));
    }

    @EventHandler
    public void on(SKUCreatedEvent event) {
        Warehouse warehouse = warehouseRepo.findByName(event.warehouseId());
        if (warehouse == null) throw new RuntimeException(String.format("WTF Warehouse %s not found", event.warehouseId()));

        SKU sku = skuRepo.save(new SKU(warehouse, event.skuName(), event.quantity(), event.pricePerItem()));
        warehouse.getSkus().add(sku);
        warehouseRepo.save(warehouse);
    }

    @EventHandler
    public void on(SKUQuantityChangedEvent event) {
        Warehouse warehouse = warehouseRepo.findByName(event.warehouseId());
        if (warehouse == null) throw new RuntimeException(String.format("WTF Warehouse %s not found", event.warehouseId()));

        SKU sku = skuRepo.findByWarehouseAndSkuName(warehouse, event.skuName());
        if (sku == null) throw new RuntimeException(String.format("WTF SKU %s not found in Warehouse %s", event.skuName(), event.warehouseId()));

        sku.setQuantity(event.newQuantity());
        skuRepo.save(sku);
    }
}
