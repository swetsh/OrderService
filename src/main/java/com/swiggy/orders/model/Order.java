package com.swiggy.orders.model;

import com.swiggy.orders.dto.OrderResponse;
import com.swiggy.orders.state.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private List<Integer> menuItemsIds;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(List<Integer> menuItemsIds, User user) {
        this.menuItemsIds = menuItemsIds;
        this.user = user;
        this.orderStatus = OrderStatus.CREATED;
    }

    public Object toDto() {
        return new OrderResponse(this);
    }
}

