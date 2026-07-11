package com.archi.car_dealership_backend.vehicle.service;

import com.archi.car_dealership_backend.auth.exception.ResourceNotFoundException;
import com.archi.car_dealership_backend.entity.Vehicle;
import com.archi.car_dealership_backend.entity.VehicleStatus;
import com.archi.car_dealership_backend.repository.VehicleRepository;
import com.archi.car_dealership_backend.vehicle.dto.VehicleRequest;
import com.archi.car_dealership_backend.vehicle.dto.VehicleResponse;
import com.archi.car_dealership_backend.vehicle.mapper.VehicleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
    @Override
    public List<VehicleResponse> listVehicles() {

        return vehicleRepository.findAll()
                .stream()
                .map(VehicleMapper::toResponse)
                .toList();
    }
    @Override
    public VehicleResponse updateVehicle(UUID id, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        vehicle.setMake(request.make());
        vehicle.setModel(request.model());
        vehicle.setCategory(request.category());
        vehicle.setPrice(request.price());
        vehicle.setQuantity(request.quantity());
        // Note: status and createdAt are NOT updated

        Vehicle updated = vehicleRepository.save(vehicle);
        return VehicleMapper.toResponse(updated);
    }
    @Override
    public VehicleResponse getVehicleById(UUID id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found with id: " + id));

        return VehicleMapper.toResponse(vehicle);
    }
    @Override
    public void deleteVehicle(UUID id) {

        Vehicle vehicle =
                vehicleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Vehicle not found with id: " + id
                                ));

        vehicle.setStatus(VehicleStatus.DISCONTINUED);

        vehicleRepository.save(vehicle);
    }

}