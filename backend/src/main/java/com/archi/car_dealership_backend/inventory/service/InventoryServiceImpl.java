package com.archi.car_dealership_backend.inventory.service;

import com.archi.car_dealership_backend.auth.exception.ResourceNotFoundException;
import com.archi.car_dealership_backend.entity.*;
import com.archi.car_dealership_backend.repository.InventoryTransactionRepository;
import com.archi.car_dealership_backend.repository.UserRepository;
import com.archi.car_dealership_backend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final VehicleRepository vehicleRepository;

    private final UserRepository userRepository;

    private final InventoryTransactionRepository transactionRepository;

    @Override
    public void purchaseVehicle(UUID vehicleId,
                                int quantity,
                                String email){

        Vehicle vehicle =
                vehicleRepository.findById(vehicleId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Vehicle not found"
                                ));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (vehicle.getQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock"
            );
        }

        vehicle.setQuantity(
                vehicle.getQuantity() - quantity
        );

        vehicleRepository.save(vehicle);

        InventoryTransaction transaction =
                InventoryTransaction.builder()
                        .vehicle(vehicle)
                        .performedBy(user)
                        .type(TransactionType.PURCHASE)
                        .quantityChange(-quantity)
                        .build();

        transactionRepository.save(transaction);
    }
}