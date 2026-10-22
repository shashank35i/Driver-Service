package com.goon.driverservice.security;

import com.goon.driverservice.entity.Driver;
import com.goon.driverservice.repository.DriverRepository;
import com.goon.driverservice.service.DriverApprovalStatus;
import com.goon.driverservice.service.LoginRequest;
import com.goon.driverservice.service.RegisterRequest;
import com.goon.driverservice.service.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final DriverRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtSigner jwtSigner;

    public String login(LoginRequest request) {
        Driver driver = repository.findByEmailOrPhone(
                        request.identifier(),
                        request.identifier())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(
                request.password(), driver.getPasswordHash())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        if (!driver.isEnabled()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Account disabled");
        }

        return jwtSigner.generateToken(driver.getDriverId(), Role.DRIVER);
    }

    public String register(RegisterRequest request) {

        if (request.name() == null || request.name().isBlank()
                || request.email() == null || request.email().isBlank()
                || request.phone() == null || request.phone().isBlank()
                || request.password() == null || request.password().isBlank()
                || request.licenseNumber() == null || request.licenseNumber().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "All fields are required");
        }

        String email = request.email().trim().toLowerCase();
        String license = request.licenseNumber().trim().toUpperCase();

        if (repository.existsByPhone(request.phone())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Phone already registered");
        }

        if (repository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email already registered");
        }

        if (repository.existsByLicenseNumber(license)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "License already registered");
        }

        Driver driver = Driver.builder()
                .name(request.name().trim())
                .email(email)
                .phone(request.phone())
                .passwordHash(passwordEncoder.encode(request.password()))
                .licenseNumber(license)
                .approvalStatus(DriverApprovalStatus.PENDING)
                .build();

        repository.save(driver);

        return "Registration Success";
    }
}