package com.goon.driverservice.service;

import java.util.UUID;

public record StartRequest(UUID driverId, String otp) {
}
