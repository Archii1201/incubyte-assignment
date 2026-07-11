package com.archi.car_dealership_backend.vehicle.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record VehicleResponse(
        UUID id,
        String make,
        String model,
        String category,
        BigDecimal price,
        Integer quantity,
        String status
) {}