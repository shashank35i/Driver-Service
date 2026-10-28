package com.goon.driverservice.controller;

import com.goon.driverservice.dto.request.AcceptOfferRequest;
import com.goon.driverservice.dto.response.AcceptOfferResponse;
import com.goon.driverservice.entity.Driver;
import com.goon.driverservice.entity.Vehicle;
import com.goon.driverservice.repository.DriverRepository;
import com.goon.driverservice.repository.DriverAssignmentRepository;
import com.goon.driverservice.repository.VehicleRepository;
import com.goon.driverservice.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {

    private final RideServiceClient rideServiceClient;
    private final DriverRepository driverRepository;
    private final DriverAssignmentRepository driverAssignmentRepository;
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

        if (driverAssignmentRepository.reserve(driverId, offerId, java.time.Instant.now()) != 1) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Driver already has an active or pending ride assignment");
        }

        try {
            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Driver not found"));

            Vehicle vehicle = vehicleRepository.findByDriverId(driverId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Vehicle not found"));

            AcceptOfferRequest request = new AcceptOfferRequest(
                    driverId,
                    driver.getName(),
                    vehicle.getVehicleType(),
                    vehicle.getVehicleNumber()
            );

            AcceptOfferResponse accepted = rideServiceClient.acceptOffer(offerId, request);
            if (driverAssignmentRepository.activate(driverId, offerId, accepted.rideId()) != 1) {
                throw new IllegalStateException("Could not activate driver ride reservation");
            }
            return ResponseEntity.ok(accepted);
        } catch (HttpClientErrorException ex) {
            // A definitive RideService 4xx means this offer was not accepted.
            driverAssignmentRepository.releaseReservation(driverId, offerId);
            throw ex;
        } catch (ResponseStatusException ex) {
            // Local validation failed before the request was sent to RideService.
            driverAssignmentRepository.releaseReservation(driverId, offerId);
            throw ex;
        }
    }
}
