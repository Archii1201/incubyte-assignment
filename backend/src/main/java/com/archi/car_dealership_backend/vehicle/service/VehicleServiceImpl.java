package com.archi.car_dealership_backend.vehicle.service;

import com.archi.car_dealership_backend.entity.Vehicle;
import com.archi.car_dealership_backend.repository.VehicleRepository;
import com.archi.car_dealership_backend.vehicle.dto.VehicleRequest;
import com.archi.car_dealership_backend.vehicle.dto.VehicleResponse;
import com.archi.car_dealership_backend.vehicle.mapper.VehicleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public VehicleResponse createVehicle(VehicleRequest request) {

        Vehicle vehicle =
                VehicleMapper.toEntity(request);

        Vehicle savedVehicle =
                vehicleRepository.save(vehicle);

        return VehicleMapper.toResponse(savedVehicle);
    }
}