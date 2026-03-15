package com.oceanview.repository;

import com.oceanview.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Check overlapping reservations for a room
    List<Reservation> findByRoomIdAndCheckOutAfterAndCheckInBefore(
            Integer roomId,
            LocalDate checkIn,
            LocalDate checkOut
    );

    List<Reservation> findByCheckOutAfterAndCheckInBefore(
            LocalDate checkIn,
            LocalDate checkOut
    );
    List<Reservation> findByIsDeletedFalse();
    @Query("""
    SELECT r FROM Reservation r
    WHERE r.roomId = :roomId
    AND r.checkIn <= :today
    AND r.checkOut >= :today
    AND r.isDeleted = false
    """)
    Optional<Reservation> findActiveReservation(Integer roomId, LocalDate today);

    @Query("""
    SELECT COUNT(r)
    FROM Reservation r
    WHERE r.isDeleted = false
    """)
    Long countActiveReservations();

    @Query("""
    SELECT COALESCE(SUM(r.totalBill),0)
    FROM Reservation r
    WHERE DATE(r.createdDate) = CURRENT_DATE
    AND r.isDeleted = false
    """)
    BigDecimal sumTodayRevenue();
}