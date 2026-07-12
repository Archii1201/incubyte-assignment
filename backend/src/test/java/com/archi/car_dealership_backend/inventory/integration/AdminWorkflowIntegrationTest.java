package com.archi.car_dealership_backend.inventory.integration;

import com.archi.car_dealership_backend.entity.*;
import com.archi.car_dealership_backend.inventory.service.InventoryService;
import com.archi.car_dealership_backend.repository.InventoryTransactionRepository;
import com.archi.car_dealership_backend.repository.UserRepository;
import com.archi.car_dealership_backend.repository.VehicleRepository;
import com.archi.car_dealership_backend.vehicle.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
public class AdminWorkflowIntegrationTest {
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
    private VehicleService vehicleService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryTransactionRepository transactionRepository;
    @Test
    void adminWorkflow_shouldCompleteSuccessfully() {
        User admin = userRepository.save(

                User.builder()
                        .name("Admin")
                        .email("admin@test.com")
                        .password("password")
                        .role(Role.ADMIN)
                        .build()

        );
        Vehicle vehicle = vehicleRepository.save(

                Vehicle.builder()
                        .make("Toyota")
                        .model("Fortuner")
                        .category("SUV")
                        .price(new BigDecimal("45000"))
                        .quantity(5)
                        .status(VehicleStatus.ACTIVE)
                        .build()

        );
        inventoryService.restockVehicle(
                vehicle.getId(),
                5,
                admin.getEmail()
        );

        Vehicle afterRestock =
                vehicleRepository.findById(vehicle.getId())
                        .orElseThrow();

        assertThat(afterRestock.getQuantity())
                .isEqualTo(10);

        assertThat(afterRestock.getStatus())
                .isEqualTo(VehicleStatus.ACTIVE);
        inventoryService.purchaseVehicle(
                vehicle.getId(),
                3,
                admin.getEmail()
        );

        Vehicle afterPurchase =
                vehicleRepository.findById(vehicle.getId())
                        .orElseThrow();

        assertThat(afterPurchase.getQuantity())
                .isEqualTo(7);
        List<InventoryTransaction> transactions =
                transactionRepository
                        .findByVehicleIdOrderByTimestampDesc(
                                vehicle.getId()
                        );

        assertThat(transactions)
                .hasSize(2);

        assertThat(transactions)
                .extracting(InventoryTransaction::getType)
                .containsExactlyInAnyOrder(
                        TransactionType.RESTOCK,
                        TransactionType.PURCHASE
                );
        vehicleService.deleteVehicle(
                vehicle.getId()
        );

        Vehicle deletedVehicle =
                vehicleRepository.findById(vehicle.getId())
                        .orElseThrow();

        assertThat(deletedVehicle.getStatus())
                .isEqualTo(VehicleStatus.DISCONTINUED);
    }
}
