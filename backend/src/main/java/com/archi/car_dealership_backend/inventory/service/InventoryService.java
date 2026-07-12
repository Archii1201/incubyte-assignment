package com.archi.car_dealership_backend.inventory.service;

import com.archi.car_dealership_backend.inventory.dto.PurchaseResponse;
import com.archi.car_dealership_backend.inventory.dto.RestockResponse;

import java.util.UUID;

public interface InventoryService {

    PurchaseResponse purchaseVehicle(
            UUID vehicleId,
            int quantity,
            String email
    );




    RestockResponse restockVehicle(
            UUID vehicleId,
            int quantity,
            String email
    );

}