package com.swiggy.orders.model;

import com.swiggy.orders.dto.DeliveryPersonResponse;
import com.swiggy.orders.utils.Location;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class DeliveryPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;


    @Embedded
    private Location location;

    private Order order;

    public DeliveryPerson(String username, Location location) {
        this.username = username;
        this.location = location;
    }

    public void assignOrder(Order order) {
        this.order = order;
    }

    public DeliveryPersonResponse toDto() {
        return new DeliveryPersonResponse(this);
    }
}
