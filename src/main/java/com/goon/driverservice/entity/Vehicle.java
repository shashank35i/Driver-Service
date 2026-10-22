package com.goon.driverservice.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private UUID vehicleId;

    @Column(nullable = false)
    private UUID driverId;

    @Column(nullable = false)
    private String vehicleModel;

    @Column(nullable = false, unique = true)
    private String vehicleNumber;
}