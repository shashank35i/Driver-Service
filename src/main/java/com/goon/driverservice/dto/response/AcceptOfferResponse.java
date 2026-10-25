package com.goon.driverservice.dto.response;

import java.util.UUID;

public record AcceptOfferResponse(
        UUID rideId,
        String status
) {}