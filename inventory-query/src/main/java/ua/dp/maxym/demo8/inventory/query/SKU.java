package ua.dp.maxym.demo8.inventory.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class SKU {

    private Long id;
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

    @SuppressWarnings("unused")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    public Warehouse getWarehouse() {
        return warehouse;
    }

    @SuppressWarnings("unused")
    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public String toString() {
        return "SKU{" +
                "id=" + id +
                ", skuName='" + skuName + '\'' +
                ", quantity=" + quantity +
                ", pricePerItem=" + pricePerItem +
                '}';
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getSkuName() {
        return skuName;
    }

    @SuppressWarnings("unused")
    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @SuppressWarnings("unused")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @SuppressWarnings("unused")
    public Double getPricePerItem() {
        return pricePerItem;
    }

    @SuppressWarnings("unused")
    public void setPricePerItem(Double pricePerItem) {
        this.pricePerItem = pricePerItem;
    }

}
