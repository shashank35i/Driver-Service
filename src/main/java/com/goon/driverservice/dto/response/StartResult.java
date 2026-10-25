package com.goon.driverservice.dto.response;


import java.time.Instant;

public record StartResult(
        boolean success,
        String status,
        Instant startedAt
) {}
