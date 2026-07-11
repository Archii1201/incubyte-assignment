package com.archi.car_dealership_backend.vehicle.controller;

import com.archi.car_dealership_backend.vehicle.dto.VehicleRequest;
import com.archi.car_dealership_backend.vehicle.dto.VehicleResponse;
import com.archi.car_dealership_backend.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VehicleResponse> createVehicle(@Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.createVehicle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}