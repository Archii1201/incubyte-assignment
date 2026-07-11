package com.archi.car_dealership_backend.vehicle.service;

import com.archi.car_dealership_backend.vehicle.dto.VehicleRequest;
import com.archi.car_dealership_backend.vehicle.dto.VehicleResponse;

public interface VehicleService {

    VehicleResponse createVehicle(VehicleRequest request);

}