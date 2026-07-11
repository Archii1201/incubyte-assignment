package com.archi.car_dealership_backend.vehicle.repository;

import com.archi.car_dealership_backend.vehicle.dto.VehicleSearchRequest;
import com.archi.car_dealership_backend.entity.Vehicle;
import com.archi.car_dealership_backend.entity.VehicleStatus;
import com.archi.car_dealership_backend.repository.VehicleRepository;
import com.archi.car_dealership_backend.repository.VehicleSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        vehicleRepository.deleteAll();
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
        vehicleRepository.deleteAll();
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
    @Test
    void update_existingVehicle_succeeds() {
        Vehicle vehicle = Vehicle.builder()
                .make("Toyota").model("Corolla").category("Sedan")
                .price(new BigDecimal("22000")).quantity(5)
                .status(VehicleStatus.ACTIVE)
                .build();
        Vehicle saved = vehicleRepository.save(vehicle);

        saved.setPrice(new BigDecimal("21500"));
        saved.setQuantity(4);
        Vehicle updated = vehicleRepository.save(saved);

        Optional<Vehicle> fetched = vehicleRepository.findById(updated.getId());
        assertThat(fetched).isPresent();
        assertThat(fetched.get().getPrice()).isEqualTo(new BigDecimal("21500"));
        assertThat(fetched.get().getQuantity()).isEqualTo(4);
    }

    @Test
    void findById_returnsEmpty_whenVehicleNotFound() {
        Optional<Vehicle> result = vehicleRepository.findById(UUID.randomUUID());
        assertThat(result).isEmpty();
    }
    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
        vehicleRepository.saveAll(List.of(
                Vehicle.builder().make("Toyota").model("Corolla").category("Sedan")
                        .price(new BigDecimal("22000")).quantity(5).status(VehicleStatus.ACTIVE).build(),
                Vehicle.builder().make("Toyota").model("Camry").category("Sedan")
                        .price(new BigDecimal("28000")).quantity(3).status(VehicleStatus.ACTIVE).build(),
                Vehicle.builder().make("Honda").model("Civic").category("Sedan")
                        .price(new BigDecimal("24000")).quantity(7).status(VehicleStatus.ACTIVE).build(),
                Vehicle.builder().make("Ford").model("F-150").category("Truck")
                        .price(new BigDecimal("35000")).quantity(2).status(VehicleStatus.ACTIVE).build(),
                Vehicle.builder().make("Tesla").model("Model 3").category("Sedan")
                        .price(new BigDecimal("45000")).quantity(0).status(VehicleStatus.DISCONTINUED).build()
        ));
    }
    @Test
    void searchByMake_filtersByMakeIgnoringCase() {
        Specification<Vehicle> spec = VehicleSpecification.search(
                new VehicleSearchRequest("toyota", null, null, null, null, null, null));
        Page<Vehicle> result = vehicleRepository.findAll(spec, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).allMatch(v -> v.getMake().equalsIgnoreCase("Toyota"));
    }

    @Test
    void searchByModel_partialMatch() {

        Specification<Vehicle> spec = VehicleSpecification.search(
                new VehicleSearchRequest(null, "o", null, null, null, null, null));
        Page<Vehicle> result = vehicleRepository.findAll(spec, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1); // Corolla, Model 3 (but 3 is discontinued, so only Corolla)
    }
    @Test
    void searchByCategory_exactMatch() {
        Specification<Vehicle> spec = VehicleSpecification.search(
                new VehicleSearchRequest(null, null, "Truck", null, null, null, null));
        Page<Vehicle> result = vehicleRepository.findAll(spec, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getModel()).isEqualTo("F-150");
    }

    @Test
    void searchByPriceRange_minAndMax() {
        Specification<Vehicle> spec = VehicleSpecification.search(
                new VehicleSearchRequest(null, null, null, new BigDecimal("20000"), new BigDecimal("30000"), null, null));
        Page<Vehicle> result = vehicleRepository.findAll(spec, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(3); // Corolla (22k), Camry (28k), Civic (24k)
        assertThat(result.getContent()).allMatch(v ->
                v.getPrice().compareTo(new BigDecimal("20000")) >= 0 &&
                        v.getPrice().compareTo(new BigDecimal("30000")) <= 0);
    }
    @Test
    void search_combinedFilters() {
        Specification<Vehicle> spec = VehicleSpecification.search(
                new VehicleSearchRequest("Toyota", null, "Sedan", new BigDecimal("20000"), new BigDecimal("30000"), null, null));
        Page<Vehicle> result = vehicleRepository.findAll(spec, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(2); // Corolla (22k), Camry (28k)
    }

    @Test
    void search_noResults() {
        Specification<Vehicle> spec = VehicleSpecification.search(
                new VehicleSearchRequest("Ferrari", null, null, null, null, null, null));
        Page<Vehicle> result = vehicleRepository.findAll(spec, PageRequest.of(0, 10));

        assertThat(result.getContent()).isEmpty();
    }
    @Test
    void search_excludesDiscontinued() {
        Specification<Vehicle> spec = VehicleSpecification.search(
                new VehicleSearchRequest(null, null, "Sedan", null, null, null, null));
        Page<Vehicle> result = vehicleRepository.findAll(spec, PageRequest.of(0, 10));

        // Should have Corolla, Camry, Civic (3), but NOT Tesla (discontinued)
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent()).noneMatch(v -> v.getMake().equals("Tesla"));
    }

    @Test
    void search_pagination() {
        Specification<Vehicle> spec = VehicleSpecification.search(
                new VehicleSearchRequest(null, null, "Sedan", null, null, null, null));
        Page<Vehicle> page1 = vehicleRepository.findAll(spec, PageRequest.of(0, 2));
        Page<Vehicle> page2 = vehicleRepository.findAll(spec, PageRequest.of(1, 2));

        assertThat(page1.getContent()).hasSize(2);
        assertThat(page2.getContent()).hasSize(1);
        assertThat(page1.hasNext()).isTrue();
    }
}
