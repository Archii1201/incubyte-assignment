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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VehicleResponse> createVehicle(@Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.createVehicle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getAllVehicles() {

        List<VehicleResponse> vehicles =
                vehicleService.listVehicles();

        return ResponseEntity.ok(vehicles);
    }
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getVehicleById(

            @PathVariable UUID id
    ) {

        VehicleResponse response =
                vehicleService.getVehicleById(id);

        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VehicleResponse> updateVehicle(

            @PathVariable
            UUID id,

            @Valid
            @RequestBody
            VehicleRequest request
    ) {

        VehicleResponse response =
                vehicleService.updateVehicle(id, request);

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVehicle(

            @PathVariable
            UUID id
    ) {

        vehicleService.deleteVehicle(id);

        return ResponseEntity.noContent().build();
    }
}