package com.goon.driverservice.service;

import java.util.UUID;

public record AcceptOfferResponse(
        UUID rideId,
        String status
) {}