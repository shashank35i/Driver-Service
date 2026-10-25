package com.goon.driverservice.dto.request;

public record
LoginRequest(
        String identifier,
        String password
) {
}
