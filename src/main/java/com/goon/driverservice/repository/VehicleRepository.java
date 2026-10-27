package com.goon.driverservice.repository;

import com.goon.driverservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    Optional<Vehicle> findByDriverId(UUID driverId);
    boolean existsByDriverId(UUID driverId);
}