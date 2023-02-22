package ua.dp.maxym.demo8.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class OrderItem {

    private Long id;
    private Order order;
    private String skuName;
    private Integer quantity;

    public OrderItem() {
    }

    public OrderItem(Order order, String skuName, Integer quantity) {
        this.order = order;
        this.skuName = skuName;
        this.quantity = quantity;
    }

    @SuppressWarnings("unused")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    public Order getOrder() {
        return order;
    }

    @SuppressWarnings("unused")
    public void setOrder(Order order) {
        this.order = order;
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

    @SuppressWarnings("unused")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
