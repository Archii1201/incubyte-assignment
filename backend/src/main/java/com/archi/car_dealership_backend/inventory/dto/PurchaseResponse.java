package com.archi.car_dealership_backend.inventory.dto;

import lombok.Builder;

@Builder
public record PurchaseResponse(

        String message,

        int remainingStock

) {
}