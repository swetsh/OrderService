package com.swiggy.orders.utils;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Location {
    @Column(unique = true)
    private String geoCoordinate;


    public Location(String geoCoordinate) {
        this.geoCoordinate = geoCoordinate;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof Location otherLocation)) {
            return false;
        }

        return geoCoordinate.equals(otherLocation.geoCoordinate);
    }
}
