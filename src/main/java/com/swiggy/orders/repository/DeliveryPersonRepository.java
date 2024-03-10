package com.swiggy.orders.repository;

import com.swiggy.orders.model.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, Integer> {
}
