package com.archi.car_dealership_backend.vehicle.service;

import com.archi.car_dealership_backend.vehicle.dto.VehicleRequest;
import com.archi.car_dealership_backend.vehicle.dto.VehicleResponse;
import com.archi.car_dealership_backend.vehicle.dto.VehicleSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface VehicleService {

    VehicleResponse createVehicle(VehicleRequest request);
    public List<VehicleResponse> listVehicles();
    VehicleResponse getVehicleById(UUID id);
    VehicleResponse updateVehicle(UUID id, VehicleRequest request);
    void deleteVehicle(UUID id);
    Page<VehicleResponse> searchVehicles(VehicleSearchRequest request);
}