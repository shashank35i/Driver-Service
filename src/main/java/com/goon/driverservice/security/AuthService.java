package com.goon.driverservice.security;


import com.goon.driverservice.entity.Driver;
import com.goon.driverservice.repository.DriverRepository;
import com.goon.driverservice.service.LoginRequest;
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


    public String login(LoginRequest request){
        Driver driver = repository.findByEmailOrPhone(
                        request.identifier(),
                        request.identifier())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid Credentials"));

        if(!passwordEncoder.matches(
                request.password(),driver.getPasswordHash()
        )){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials");
        }

        if(!driver.isEnabled()){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Account disabled");
        }

        return jwtSigner.generateToken(driver.getDriverId(), Role.CUSTOMER);
    }
}
