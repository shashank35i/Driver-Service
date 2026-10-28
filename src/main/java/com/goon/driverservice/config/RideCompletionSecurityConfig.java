package com.goon.driverservice.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class RideCompletionSecurityConfig {
    @Bean @Order(1)
    SecurityFilterChain rideCompletionSecurity(HttpSecurity http) throws Exception {
        return http.securityMatcher("/api/driver/rides/*/end")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/driver/rides/*/end")
                        .access((authentication, context) -> {
                            var current = authentication.get();
                            return new AuthorizationDecision(current instanceof JwtAuthenticationToken jwt
                                    && "goon-services".equals(jwt.getToken().getClaimAsString("azp"))
                                    && jwt.getAuthorities().stream()
                                    .anyMatch(a -> "SCOPE_driver.access".equals(a.getAuthority())));
                        }).anyRequest().denyAll())
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults())).build();
    }
}

