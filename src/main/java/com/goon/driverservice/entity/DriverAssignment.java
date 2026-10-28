package com.goon.driverservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

/** Driver-side reservation made before forwarding an offer acceptance. */
@Entity
@Table(name = "driver_assignments", uniqueConstraints = {
        @UniqueConstraint(name = "uk_driver_assignments_offer_id", columnNames = "offer_id"),
        @UniqueConstraint(name = "uk_driver_assignments_ride_id", columnNames = "ride_id")
})
public class DriverAssignment {

    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "driver_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID driverId;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "offer_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID offerId;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "ride_id", columnDefinition = "BINARY(16)")
    private UUID rideId;

    @Column(name = "status", nullable = false, length = 16)
    private String status;

    @Column(name = "claimed_at", nullable = false)
    private Instant claimedAt;

    protected DriverAssignment() {}
}
