package ua.dp.maxym.demo8.inventory.query;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SKURepository extends JpaRepository<SKU, Long> {
    SKU findByWarehouseAndSkuName(Warehouse warehouse, String skuName);
}
