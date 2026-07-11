package com.archi.car_dealership_backend.inventory.service;

import java.util.UUID;

public interface InventoryService {

    void purchaseVehicle(
            UUID vehicleId,
            int quantity,
            String email
    );




    void restockVehicle(
            UUID vehicleId,
            int quantity,
            String email
    );

}