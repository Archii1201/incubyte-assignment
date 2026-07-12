package com.archi.car_dealership_backend.inventory.service;

import com.archi.car_dealership_backend.auth.exception.ResourceNotFoundException;
import com.archi.car_dealership_backend.entity.*;
import com.archi.car_dealership_backend.inventory.dto.PurchaseResponse;
import com.archi.car_dealership_backend.inventory.dto.RestockResponse;
import com.archi.car_dealership_backend.repository.InventoryTransactionRepository;
import com.archi.car_dealership_backend.repository.UserRepository;
import com.archi.car_dealership_backend.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.archi.car_dealership_backend.auth.exception.BusinessRuleException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final VehicleRepository vehicleRepository;

    private final UserRepository userRepository;

    private final InventoryTransactionRepository transactionRepository;

    @Override
    @Transactional
    public PurchaseResponse purchaseVehicle(UUID vehicleId,
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
        if (quantity <= 0) {
            throw new BusinessRuleException(
                    "Quantity must be greater than zero"
            );
        }
        if (vehicle.getQuantity() < quantity) {
            throw new BusinessRuleException(
                    "Insufficient stock"
            );
        }

        vehicle.setQuantity(
                vehicle.getQuantity() - quantity
        );
        if (vehicle.getQuantity() == 0) {
            vehicle.setStatus(VehicleStatus.OUT_OF_STOCK);
        }
        vehicleRepository.save(vehicle);

        InventoryTransaction transaction =
                InventoryTransaction.builder()
                        .vehicle(vehicle)
                        .performedBy(user)
                        .type(TransactionType.PURCHASE)
                        .quantityChange(-quantity)
                        .build();

        transactionRepository.save(transaction);
        return PurchaseResponse.builder()
                .message("Purchase successful")
                .remainingStock(vehicle.getQuantity())
                .build();
    }
    @Override
    @Transactional
    public RestockResponse restockVehicle(
            UUID vehicleId,
            int quantity,
            String email
    ) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        vehicle.setQuantity(
                vehicle.getQuantity() + quantity
        );

        vehicle.setQuantity(
                vehicle.getQuantity() + quantity
        );

        if (vehicle.getQuantity() > 0 &&
                vehicle.getStatus() != VehicleStatus.DISCONTINUED) {

            vehicle.setStatus(VehicleStatus.ACTIVE);
        }

        vehicleRepository.save(vehicle);

        InventoryTransaction transaction =
                InventoryTransaction.builder()
                        .vehicle(vehicle)
                        .performedBy(user)
                        .type(TransactionType.RESTOCK)
                        .quantityChange(quantity)
                        .build();

        transactionRepository.save(transaction);
        return RestockResponse.builder()
                .message("Vehicle restocked successfully")
                .currentStock(vehicle.getQuantity())
                .build();
    }
}