package com.archi.car_dealership_backend.vehicle.service;

import com.archi.car_dealership_backend.auth.exception.ResourceNotFoundException;
import com.archi.car_dealership_backend.entity.Vehicle;
import com.archi.car_dealership_backend.entity.VehicleStatus;
import com.archi.car_dealership_backend.repository.VehicleRepository;
import com.archi.car_dealership_backend.vehicle.dto.VehicleRequest;
import com.archi.car_dealership_backend.vehicle.dto.VehicleResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    void createVehicle_success_returnsResponseWithActiveStatus() {

        VehicleRequest request =
                new VehicleRequest(
                        "Toyota",
                        "Corolla",
                        "Sedan",
                        new BigDecimal("22000.00"),
                        5
                );

        when(vehicleRepository.save(any(Vehicle.class)))
                .thenAnswer(invocation -> {

                    Vehicle vehicle = invocation.getArgument(0);
                    vehicle.setId(UUID.randomUUID());

                    return vehicle;
                });

        VehicleResponse response =
                vehicleService.createVehicle(request);

        assertThat(response.id()).isNotNull();

        assertThat(response.make())
                .isEqualTo("Toyota");

        assertThat(response.status())
                .isEqualTo("ACTIVE");

        verify(vehicleRepository)
                .save(argThat(vehicle ->
                        vehicle.getStatus() == VehicleStatus.ACTIVE));
    }
    @Test
    void listVehicles_returnsEmptyList_whenNoVehiclesExist() {

        when(vehicleRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<VehicleResponse> result =
                vehicleService.listVehicles();

        assertThat(result)
                .isEmpty();
    }
    @Test
    void listVehicles_returnsAllVehicles() {

        Vehicle vehicle1 = Vehicle.builder()
                .id(UUID.randomUUID())
                .make("Toyota")
                .model("Corolla")
                .category("Sedan")
                .price(new BigDecimal("22000"))
                .quantity(5)
                .status(VehicleStatus.ACTIVE)
                .build();

        Vehicle vehicle2 = Vehicle.builder()
                .id(UUID.randomUUID())
                .make("Honda")
                .model("Civic")
                .category("Sedan")
                .price(new BigDecimal("24000"))
                .quantity(3)
                .status(VehicleStatus.ACTIVE)
                .build();

        when(vehicleRepository.findAll())
                .thenReturn(List.of(vehicle1, vehicle2));

        List<VehicleResponse> result =
                vehicleService.listVehicles();

        assertThat(result)
                .hasSize(2);

        assertThat(result.get(0).make())
                .isEqualTo("Toyota");

        assertThat(result.get(1).make())
                .isEqualTo("Honda");
    }
    @Test
    void updateVehicle_success_returnsUpdatedResponse() {
        UUID vehicleId = UUID.randomUUID();
        Vehicle existing = Vehicle.builder()
                .id(vehicleId)
                .make("Toyota").model("Corolla").category("Sedan")
                .price(new BigDecimal("22000")).quantity(5)
                .status(VehicleStatus.ACTIVE)
                .version(0)
                .build();

        VehicleRequest updateRequest = new VehicleRequest("Toyota", "Corolla Updated",
                "Sedan", new BigDecimal("21500"), 4);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existing));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));

        VehicleResponse result = vehicleService.updateVehicle(vehicleId, updateRequest);

        assertThat(result.model()).isEqualTo("Corolla Updated");
        assertThat(result.price()).isEqualByComparingTo(new BigDecimal("21500"));
        assertThat(result.quantity()).isEqualTo(4);
        verify(vehicleRepository).save(argThat(v -> v.getModel().equals("Corolla Updated")));
    }
    @Test
    void updateVehicle_throwsNotFoundException_whenVehicleNotFound() {
        UUID vehicleId = UUID.randomUUID();
        VehicleRequest updateRequest = new VehicleRequest("Toyota", "Corolla",
                "Sedan", new BigDecimal("22000"), 5);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.updateVehicle(vehicleId, updateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Vehicle not found");

        verify(vehicleRepository, never()).save(any());
    }
    @Test
    void updateVehicle_preservesCreatedAt() {
        UUID vehicleId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        Vehicle existing = Vehicle.builder()
                .id(vehicleId)
                .make("Toyota").model("Corolla").category("Sedan")
                .price(new BigDecimal("22000")).quantity(5)
                .status(VehicleStatus.ACTIVE)
                .createdAt(createdAt)
                .build();

        VehicleRequest updateRequest = new VehicleRequest("Toyota", "Corolla",
                "Sedan", new BigDecimal("21000"), 5);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(existing));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));

        vehicleService.updateVehicle(vehicleId, updateRequest);

        verify(vehicleRepository).save(argThat(v -> v.getCreatedAt().equals(createdAt)));
    }

}