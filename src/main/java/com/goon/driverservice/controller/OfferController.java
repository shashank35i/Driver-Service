package com.goon.driverservice.controller;

import com.goon.driverservice.entity.Driver;
import com.goon.driverservice.repository.DriverRepository;
import com.goon.driverservice.service.AcceptOfferRequest;
import com.goon.driverservice.service.AcceptOfferResponse;
import com.goon.driverservice.service.RideServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController("/offers")
@RequiredArgsConstructor
public class OfferController {

    private final RideServiceClient rideServiceClient;
    private final DriverRepository driverRepository;


    @PatchMapping("/{offerId}/decline")
    public ResponseEntity<Void> decline(
            @PathVariable UUID offerId,
            Authentication authentication) {

        UUID driverId = UUID.fromString(authentication.getName());

        rideServiceClient.declineOffer(offerId, driverId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{offerId}/accept")
    public ResponseEntity<AcceptOfferResponse> accept(
            @PathVariable UUID offerId,
            Authentication authentication) {

        UUID driverId = UUID.fromString(authentication.getName());

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Driver not found"));

        AcceptOfferRequest request = new AcceptOfferRequest(
                driverId,
                driver.getName(),
                driver.getVehicleModel(),
                driver.getVehicleNumber()
        );

        return ResponseEntity.ok(
                rideServiceClient.acceptOffer(offerId, request)
        );
    }
}