package com.goon.driverservice.dto.response;
import java.math.BigDecimal;
import java.util.UUID;
public record EndRideResponse(UUID rideId, String status, String paymentStatus,
                              BigDecimal finalFare, String currency) {}

