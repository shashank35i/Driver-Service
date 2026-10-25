package com.goon.driverservice;


import java.time.Instant;

public record StartResult(
        boolean success,
        String status,
        Instant startedAt
) {}
