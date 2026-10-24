package com.goon.driverservice.controller;

import com.goon.driverservice.entity.Driver;
import com.goon.driverservice.entity.Vehicle;
import com.goon.driverservice.repository.DriverRepository;
import com.goon.driverservice.repository.VehicleRepository;
import com.goon.driverservice.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {

    private final RideServiceClient rideServiceClient;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    @PatchMapping("/{offerId}/decline")
    public ResponseEntity<Void> decline(
            @PathVariable UUID offerId,
            @RequestHeader("X-User-Id") UUID driverId) {

        rideServiceClient.declineOffer(offerId, driverId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{offerId}/accept")
    public ResponseEntity<AcceptOfferResponse> accept(
            @PathVariable UUID offerId,
            @RequestHeader("X-User-Id") UUID driverId) {

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Driver not found"));

        Vehicle vehicle = vehicleRepository.findByDriverId(driverId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle not found"));

        AcceptOfferRequest request = new AcceptOfferRequest(
                driverId,
                driver.getName(),
                vehicle.getVehicleModel(),
                vehicle.getVehicleNumber()
        );

        return ResponseEntity.ok(
                rideServiceClient.acceptOffer(offerId, request)
        );
    }
}