package com.archi.car_dealership_backend.vehicle.repository;

import com.archi.car_dealership_backend.entity.Vehicle;
import com.archi.car_dealership_backend.entity.VehicleStatus;
import com.archi.car_dealership_backend.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
class VehicleRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void save_and_findById_roundTrip() {
        Vehicle vehicle = Vehicle.builder()
                .make("Toyota").model("Corolla").category("Sedan")
                .price(new BigDecimal("22000.00")).quantity(5)
                .status(VehicleStatus.ACTIVE)
                .build();

        Vehicle saved = vehicleRepository.save(vehicle);

        assertThat(saved.getId()).isNotNull();
        assertThat(vehicleRepository.findById(saved.getId())).isPresent();
    }
    @Test
    void findAll_returnsAllVehicles() {
        Vehicle v1 = Vehicle.builder().make("Toyota").model("Corolla")
                .category("Sedan").price(new BigDecimal("22000")).quantity(5)
                .status(VehicleStatus.ACTIVE).build();
        Vehicle v2 = Vehicle.builder().make("Honda").model("Civic")
                .category("Sedan").price(new BigDecimal("24000")).quantity(3)
                .status(VehicleStatus.ACTIVE).build();

        vehicleRepository.saveAll(List.of(v1, v2));

        List<Vehicle> all = vehicleRepository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void findAll_withPagination_returnsPagedResults() {
        // Save 15 vehicles
        for (int i = 0; i < 15; i++) {
            vehicleRepository.save(Vehicle.builder()
                    .make("Make" + i).model("Model" + i)
                    .category("Category").price(new BigDecimal("20000"))
                    .quantity(i).status(VehicleStatus.ACTIVE).build());
        }

        Page<Vehicle> page = vehicleRepository.findAll(PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(10);
        assertThat(page.getTotalElements()).isEqualTo(15);
        assertThat(page.hasNext()).isTrue();
    }
}
