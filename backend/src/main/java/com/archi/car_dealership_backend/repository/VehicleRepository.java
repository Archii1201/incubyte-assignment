package com.archi.car_dealership_backend.repository;

import com.archi.car_dealership_backend.entity.Vehicle;
import com.archi.car_dealership_backend.entity.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface VehicleRepository
        extends JpaRepository<Vehicle, UUID>, JpaSpecificationExecutor<Vehicle> {
    List<Vehicle> findByStatus(VehicleStatus status);

    Page<Vehicle> findByStatusNot(VehicleStatus status, Pageable pageable);
}