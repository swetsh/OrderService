package com.swiggy.orders.model;

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

    @OneToOne
    private User user;

    public Order(List<Integer> menuItemsIds, User user) {
        this.menuItemsIds = menuItemsIds;
        this.user = user;
    }
}

