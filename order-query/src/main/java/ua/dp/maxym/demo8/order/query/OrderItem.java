package ua.dp.maxym.demo8.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
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
}
