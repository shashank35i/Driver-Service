package com.goon.driverservice.dto.request;

import com.goon.driverservice.entity.VehicleType;

import java.util.UUID;

public record AcceptOfferRequest(
        UUID driverId,
        String driverName,
        VehicleType vehicleType,
        String vehicleNumber
) {}