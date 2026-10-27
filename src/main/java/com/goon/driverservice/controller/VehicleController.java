package com.goon.driverservice.controller;

import com.goon.driverservice.dto.request.VehicleRequest;
import com.goon.driverservice.entity.Vehicle;
import com.goon.driverservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/driver/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(
            @RequestHeader("X-User-Id") UUID driverId,
            @RequestBody VehicleRequest request) {

        return ResponseEntity.ok(
                vehicleService.addVehicle(driverId, request)
        );
    }
}