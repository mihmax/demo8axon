package ua.dp.maxym.demo8.order.query;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "orderProjection") // "order" is reserved keyword in H2
public final class Order {
    private Long id;
    private String orderId;
    private String userId;
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

    @SuppressWarnings("unused")
    @Id
    @GeneratedValue
    public String getOrderId() {
        return orderId;
    }

    @SuppressWarnings("unused")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @SuppressWarnings("unused")
    public String getUserId() {
        return userId;
    }

    @SuppressWarnings("unused")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @SuppressWarnings("unused")
    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @SuppressWarnings("unused")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @SuppressWarnings("unused")
    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "order")
    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    @SuppressWarnings("unused")
    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderId,
                                                              order.orderId) && Objects.equals(
                userId, order.userId) && Objects.equals(orderItems, order.orderItems) && Objects.equals(
                total, order.total) && Objects.equals(state, order.state) && Objects.equals(
                rejectionReason, order.rejectionReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, userId, orderItems, total, state, rejectionReason);
    }
}
