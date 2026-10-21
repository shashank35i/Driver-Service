package com.goon.driverservice.security;

import com.goon.driverservice.service.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtSigner {

    private final SecretKey key;

    public JwtSigner(
            @Value("${app.jwt-secret-key}") String jwtKey
    ){
        byte[] bytes = Base64.getDecoder().decode(jwtKey);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(UUID driverId, Role role){
        Instant now = Instant.now();

        return Jwts.builder()
                .header()
                    .type("JWT")
                    .and()
                .id(UUID.randomUUID().toString())
                .subject(driverId.toString())
                .issuer("Goon-Auth")
                .claim("role",role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(300)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
}
