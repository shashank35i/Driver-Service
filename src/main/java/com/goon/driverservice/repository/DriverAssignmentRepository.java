package com.goon.driverservice.repository;

import com.goon.driverservice.entity.DriverAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Transactional
public interface DriverAssignmentRepository extends JpaRepository<DriverAssignment, UUID> {

    @Modifying
    @Query(value = """
            INSERT IGNORE INTO driver_assignments
                (driver_id, offer_id, ride_id, status, claimed_at)
            VALUES (:driverId, :offerId, NULL, 'PENDING', :claimedAt)
            """, nativeQuery = true)
    int reserve(@Param("driverId") UUID driverId,
                @Param("offerId") UUID offerId,
                @Param("claimedAt") Instant claimedAt);

    @Modifying
    @Query(value = """
            UPDATE driver_assignments
            SET ride_id = :rideId, status = 'ACTIVE'
            WHERE driver_id = :driverId AND offer_id = :offerId AND status = 'PENDING'
            """, nativeQuery = true)
    int activate(@Param("driverId") UUID driverId,
                 @Param("offerId") UUID offerId,
                 @Param("rideId") UUID rideId);

    @Modifying
    @Query(value = "DELETE FROM driver_assignments WHERE driver_id = :driverId AND offer_id = :offerId",
            nativeQuery = true)
    int releaseReservation(@Param("driverId") UUID driverId, @Param("offerId") UUID offerId);

    @Modifying
    @Query(value = "DELETE FROM driver_assignments WHERE driver_id = :driverId AND ride_id = :rideId",
            nativeQuery = true)
    int releaseRide(@Param("driverId") UUID driverId, @Param("rideId") UUID rideId);
}
