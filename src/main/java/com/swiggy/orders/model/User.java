package com.swiggy.orders.model;

import com.swiggy.orders.utils.Location;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;

    private String password;

    @Embedded
    private Location location;

    public User(String username, String password, Location location) {
        this.username = username;
        this.password = password;
        this.location = location;
    }

}

