package com.archi.car_dealership_backend.vehicle.dto;

import java.math.BigDecimal;

public record VehicleSearchRequest(
        String make,
        String model,
        String category,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Integer page,
        Integer size
) {
    public VehicleSearchRequest {
        // Defaults
        page = page != null ? page : 0;
        size = size != null ? size : 10;
    }
}
