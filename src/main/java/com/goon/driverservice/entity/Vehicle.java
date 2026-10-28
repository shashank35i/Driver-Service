package com.goon.driverservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "vehicle_id", columnDefinition = "BINARY(16)")
    private UUID vehicleId;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "driver_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID driverId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private VehicleType vehicleType;

    @Column(nullable = false, unique = true)
    private String vehicleNumber;
}
