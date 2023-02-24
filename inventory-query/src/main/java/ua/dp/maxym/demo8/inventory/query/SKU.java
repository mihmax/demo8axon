package ua.dp.maxym.demo8.inventory.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SKU {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Warehouse warehouse;
    private String skuName;
    private Integer quantity;
    private Double pricePerItem;

    public SKU() {
    }

    public SKU(Warehouse warehouse, String skuName, Integer quantity, Double pricePerItem) {
        this.warehouse = warehouse;
        this.skuName = skuName;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
    }
}
