package com.archi.car_dealership_backend.vehicle.service;

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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}