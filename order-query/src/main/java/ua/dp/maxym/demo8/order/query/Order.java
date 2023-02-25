package ua.dp.maxym.demo8.order.query;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "orderProjection") // "order" is reserved keyword in H2
@Data
public final class Order {
    @Id
    @GeneratedValue
    private Long id;
    private String orderId;
    private String userId;
    @OneToMany(mappedBy = "order")
    @EqualsAndHashCode.Exclude
    private Set<OrderItem> orderItems = new HashSet<>();
    private Double total;
    private String state;
    private String rejectionReason;

    public Order(String orderId, String userId, Double total, String state, String rejectionReason) {
        this.orderId = orderId;
        this.userId = userId;
        this.total = total;
        this.state = state;
        this.rejectionReason = rejectionReason;
    }

    public Order() {
    }
}
