package com.archi.car_dealership_backend.inventory.integration;

import com.archi.car_dealership_backend.entity.Role;
import com.archi.car_dealership_backend.entity.User;
import com.archi.car_dealership_backend.entity.Vehicle;
import com.archi.car_dealership_backend.entity.VehicleStatus;
import com.archi.car_dealership_backend.inventory.service.InventoryService;
import com.archi.car_dealership_backend.repository.InventoryTransactionRepository;
import com.archi.car_dealership_backend.repository.UserRepository;
import com.archi.car_dealership_backend.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;


@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
public class OptimisticLockingIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {

        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl
        );

        registry.add(
                "spring.datasource.username",
                postgres::getUsername
        );

        registry.add(
                "spring.datasource.password",
                postgres::getPassword
        );
    }

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryTransactionRepository transactionRepository;
    @Test
    void updatingStaleEntity_shouldThrowOptimisticLockException() {
        User user = userRepository.save(
                User.builder()
                        .name("Archi")
                        .email("archi@test.com")
                        .password("password")
                        .role(Role.USER)
                        .build()
        );

        Vehicle vehicle = vehicleRepository.save(
                Vehicle.builder()
                        .make("Toyota")
                        .model("Corolla")
                        .category("Sedan")
                        .price(new BigDecimal("22000"))
                        .quantity(5)
                        .status(VehicleStatus.ACTIVE)
                        .build()
        );
        Vehicle first =
                vehicleRepository.findById(vehicle.getId()).orElseThrow();

        Vehicle second =
                vehicleRepository.findById(vehicle.getId()).orElseThrow();
        first.setQuantity(4);

        vehicleRepository.saveAndFlush(first);
        second.setQuantity(3);
        assertThatThrownBy(() -> {

            vehicleRepository.saveAndFlush(second);

        })
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

}
