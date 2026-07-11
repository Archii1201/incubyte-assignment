package com.archi.car_dealership_backend.inventory.repository;

import com.archi.car_dealership_backend.entity.*;
import com.archi.car_dealership_backend.repository.InventoryTransactionRepository;
import com.archi.car_dealership_backend.repository.VehicleRepository;
import com.archi.car_dealership_backend.repository.UserRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
class InventoryTransactionRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @Autowired
    private InventoryTransactionRepository transactionRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveTransaction_success() {

        User user = User.builder()
                .name("archi")
                .email("archi@test.com")
                .password("password")
                .role(Role.USER)
                .build();

        user = userRepository.save(user);

        Vehicle vehicle = Vehicle.builder()
                .make("Toyota")
                .model("Corolla")
                .category("Sedan")
                .price(new BigDecimal("22000"))
                .quantity(10)
                .status(VehicleStatus.ACTIVE)
                .build();

        vehicle = vehicleRepository.save(vehicle);

        InventoryTransaction transaction =
                InventoryTransaction.builder()
                        .vehicle(vehicle)
                        .performedBy(user)
                        .type(TransactionType.PURCHASE)
                        .quantityChange(-1)
                        .build();

        InventoryTransaction saved =
                transactionRepository.save(transaction);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getType())
                .isEqualTo(TransactionType.PURCHASE);
    }

    @Test
    void findByVehicleId_returnsTransactions() {

        User user = userRepository.save(
                User.builder()
                        .name("archi")
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
                        .quantity(10)
                        .status(VehicleStatus.ACTIVE)
                        .build()
        );

        transactionRepository.save(
                InventoryTransaction.builder()
                        .vehicle(vehicle)
                        .performedBy(user)
                        .type(TransactionType.PURCHASE)
                        .quantityChange(-1)
                        .build()
        );

        List<InventoryTransaction> result =
                transactionRepository.findByVehicleIdOrderByTimestampDesc(
                        vehicle.getId()
                );

        assertThat(result).hasSize(1);
    }
}