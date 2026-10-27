package com.goon.driverservice.service;

import com.goon.driverservice.dto.request.VehicleRequest;
import com.goon.driverservice.entity.Vehicle;
import com.goon.driverservice.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final StringRedisTemplate redis;

    @Transactional
    public Vehicle addVehicle(UUID driverId, VehicleRequest request) {

        if (request.vehicleType() == null ||
                request.vehicleNumber() == null ||
                request.vehicleNumber().isBlank()) {
            throw new IllegalArgumentException("Invalid vehicle details");
        }

        if (vehicleRepository.existsByDriverId(driverId)) {
            throw new IllegalStateException("Driver already has a vehicle");
        }

        Vehicle vehicle = vehicleRepository.save(
                Vehicle.builder()
                        .driverId(driverId)
                        .vehicleType(request.vehicleType())
                        .vehicleNumber(request.vehicleNumber())
                        .build()
        );

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        redis.opsForValue().set(
                                "driver:vehicle:" + driverId,
                                vehicle.getVehicleType().name()
                        );
                    }
                }
        );

        return vehicle;
    }
}