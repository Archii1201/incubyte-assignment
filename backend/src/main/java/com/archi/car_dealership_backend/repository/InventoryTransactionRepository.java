package com.archi.car_dealership_backend.repository;

import com.archi.car_dealership_backend.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, UUID> {
    List<InventoryTransaction> findByVehicleIdOrderByTimestampDesc(UUID vehicleId);
}