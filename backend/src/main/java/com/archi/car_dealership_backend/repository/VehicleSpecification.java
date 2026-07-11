package com.archi.car_dealership_backend.repository;

import com.archi.car_dealership_backend.vehicle.dto.VehicleSearchRequest;
import com.archi.car_dealership_backend.entity.Vehicle;
import com.archi.car_dealership_backend.entity.VehicleStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class VehicleSpecification {

    public static Specification<Vehicle> makeEquals(String make) {
        return (root, query, cb) ->
                make == null ? cb.conjunction() : cb.like(cb.lower(root.get("make")), "%" + make.toLowerCase() + "%");
    }

    public static Specification<Vehicle> modelContains(String model) {
        return (root, query, cb) ->
                model == null ? cb.conjunction() : cb.like(cb.lower(root.get("model")), "%" + model.toLowerCase() + "%");
    }

    public static Specification<Vehicle> categoryEquals(String category) {
        return (root, query, cb) ->
                category == null ? cb.conjunction() : cb.equal(cb.lower(root.get("category")), category.toLowerCase());
    }

    public static Specification<Vehicle> priceGreaterThanOrEqual(BigDecimal minPrice) {
        return (root, query, cb) ->
                minPrice == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Vehicle> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, cb) ->
                maxPrice == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Vehicle> statusIsNotDiscontinued() {
        return (root, query, cb) -> cb.notEqual(root.get("status"), VehicleStatus.DISCONTINUED);
    }

    /**
     * Compose all search filters. When any filter is null, it's ignored (conjunction semantics).
     * This is the core of extensibility — add new static methods above, call them here.
     */
    public static Specification<Vehicle> search(VehicleSearchRequest request) {
        return Specification.where(statusIsNotDiscontinued())
                .and(makeEquals(request.make()))
                .and(modelContains(request.model()))
                .and(categoryEquals(request.category()))
                .and(priceGreaterThanOrEqual(request.minPrice()))
                .and(priceLessThanOrEqual(request.maxPrice()));
    }
}