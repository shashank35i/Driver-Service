package com.goon.driverservice.dto.request;

import com.goon.driverservice.entity.VehicleType;

public record VehicleRequest(
        VehicleType vehicleType,
        String vehicleNumber
) {}