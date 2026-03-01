package com.oceanview.repository;

import com.oceanview.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
}