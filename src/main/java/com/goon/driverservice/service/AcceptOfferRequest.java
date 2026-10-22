package com.goon.driverservice.service;

import java.util.UUID;

public record AcceptOfferRequest(
        UUID driverId,
        String driverName,
        String vehicleModel,
        String vehicleNumber
) {}