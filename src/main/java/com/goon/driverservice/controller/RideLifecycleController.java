package com.goon.driverservice.controller;

import com.goon.driverservice.dto.response.StartResult;
import com.goon.driverservice.service.RideServiceClient;
import com.goon.driverservice.service.StartRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/driver/rides")
@RequiredArgsConstructor
public class RideLifecycleController {

    private final RideServiceClient rideServiceClient;

    @PostMapping("/{rideId}/arrived")
    public ResponseEntity<Instant> markDriverArrived(
            @PathVariable UUID rideId,
            @RequestHeader("X-User-Id") UUID driverId
    ) {
        ;

        return ResponseEntity.ok(
                rideServiceClient.markArrived(rideId, driverId)
        );
    }

    @PostMapping("/{rideId}/start")
    public ResponseEntity<StartResult> verifyStartOtp(
            @PathVariable UUID rideId,
            @RequestBody StartRequest request,
            @RequestHeader("X-User-Id") UUID driverId
    ) {


        StartResult result = rideServiceClient.start(rideId,driverId, request.otp());

        int status = result.success() ? 200
                : "OTP_LOCKED".equals(result.status()) ? 429 : 400;

        return ResponseEntity.status(status).body(result);
    }


}