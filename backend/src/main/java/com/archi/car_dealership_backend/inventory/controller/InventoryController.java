package com.archi.car_dealership_backend.inventory.controller;

import com.archi.car_dealership_backend.inventory.dto.PurchaseResponse;
import com.archi.car_dealership_backend.inventory.service.InventoryService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/{id}/purchase")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PurchaseResponse> purchaseVehicle(
            @PathVariable UUID id,
            @RequestParam @Min(1) int quantity,
            Authentication authentication
    ) {

        inventoryService.purchaseVehicle(
                id,
                quantity,
                authentication.getName()   // email
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/restock")
    public ResponseEntity<Void> restockVehicle(

            @PathVariable
            UUID id,

            @RequestParam
            @Min(1)
            int quantity,

            Authentication authentication
    ) {

        inventoryService.restockVehicle(
                id,
                quantity,
                authentication.getName()
        );

        return ResponseEntity.ok().build();
    }

}