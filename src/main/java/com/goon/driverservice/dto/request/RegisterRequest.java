package com.goon.driverservice.dto.request;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "[0-9]{10}") String phone,
        @NotBlank String password,
        @NotBlank String licenseNumber
) {}