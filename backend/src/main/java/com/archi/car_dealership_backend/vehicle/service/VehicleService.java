package com.archi.car_dealership_backend.vehicle.service;

import com.archi.car_dealership_backend.vehicle.dto.VehicleRequest;
import com.archi.car_dealership_backend.vehicle.dto.VehicleResponse;

import java.util.List;

public interface VehicleService {

    VehicleResponse createVehicle(VehicleRequest request);
    public List<VehicleResponse> listVehicles();
}