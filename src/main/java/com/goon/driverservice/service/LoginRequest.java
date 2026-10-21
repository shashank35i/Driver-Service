package com.goon.driverservice.service;

public record
LoginRequest(
        String identifier,
        String password
) {
}
