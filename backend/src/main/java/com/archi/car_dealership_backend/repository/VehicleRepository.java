package com.archi.car_dealership_backend.repository;

import com.archi.car_dealership_backend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleRepository
        extends JpaRepository<Vehicle, UUID> {

}