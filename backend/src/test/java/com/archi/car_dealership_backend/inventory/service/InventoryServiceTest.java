package com.archi.car_dealership_backend.inventory.service;

import com.archi.car_dealership_backend.auth.exception.ResourceNotFoundException;
import com.archi.car_dealership_backend.entity.*;
import com.archi.car_dealership_backend.repository.InventoryTransactionRepository;
import com.archi.car_dealership_backend.repository.UserRepository;
import com.archi.car_dealership_backend.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InventoryTransactionRepository transactionRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Test
    void purchaseVehicle_success() {

        UUID vehicleId = UUID.randomUUID();

        String email = "archi@test.com";

        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .make("Toyota")
                .quantity(5)
                .price(new BigDecimal("22000"))
                .status(VehicleStatus.ACTIVE)
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .name("archi")
                .email(email)
                .build();

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(vehicleRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        inventoryService.purchaseVehicle(
                vehicleId,
                2,
                email
        );

        assertThat(vehicle.getQuantity())
                .isEqualTo(3);

        verify(transactionRepository)
                .save(any(InventoryTransaction.class));
    }

    @Test
    void purchaseVehicle_vehicleNotFound() {

        UUID vehicleId = UUID.randomUUID();

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                inventoryService.purchaseVehicle(
                        vehicleId,
                        1,
                        "archi@test.com"
                ))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Vehicle not found");
    }

    @Test
    void purchaseVehicle_userNotFound() {

        UUID vehicleId = UUID.randomUUID();

        String email = "archi@test.com";

        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .quantity(5)
                .status(VehicleStatus.ACTIVE)
                .build();

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                inventoryService.purchaseVehicle(
                        vehicleId,
                        1,
                        email
                ))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void purchaseVehicle_notEnoughStock() {

        UUID vehicleId = UUID.randomUUID();

        String email = "archi@test.com";

        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .quantity(1)
                .status(VehicleStatus.ACTIVE)
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .build();

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() ->
                inventoryService.purchaseVehicle(
                        vehicleId,
                        5,
                        email
                ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient stock");

        verify(transactionRepository, never())
                .save(any());

        verify(vehicleRepository, never())
                .save(any());
    }
    @Test
    void restockVehicle_success_increasesQuantity() {

        UUID vehicleId = UUID.randomUUID();

        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .quantity(2)
                .status(VehicleStatus.ACTIVE)
                .build();

        User admin = User.builder()
                .email("admin@test.com")
                .role(Role.ADMIN)
                .build();

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(userRepository.findByEmail("admin@test.com"))
                .thenReturn(Optional.of(admin));

        when(vehicleRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        inventoryService.restockVehicle(
                vehicleId,
                5,
                "admin@test.com"
        );

        assertThat(vehicle.getQuantity())
                .isEqualTo(7);

        verify(transactionRepository)
                .save(any(InventoryTransaction.class));
    }
}