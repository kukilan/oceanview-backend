package com.oceanview.controller;

import com.oceanview.dto.DateRangeDTO;
import com.oceanview.dto.ReservationCalculationDTO;
import com.oceanview.dto.ReservationDTO;
import com.oceanview.dto.RoomDTO;
import com.oceanview.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @PostMapping("/available-rooms")
    public ResponseEntity<List<RoomDTO>> getAvailableRooms(
            @RequestBody DateRangeDTO dto) {

        return ResponseEntity.ok(reservationService.getAvailableRooms(dto));
    }
    // ===== Calculate Price =====
    @PostMapping("/calculate")
    public ResponseEntity<ReservationCalculationDTO> calculateReservation(
            @RequestBody ReservationCalculationDTO dto) {

        return ResponseEntity.ok(reservationService.calculateReservation(dto));
    }

    // ===== Create Reservation =====
    @PostMapping("/create")
    public ResponseEntity<ReservationDTO> createReservation(
            @RequestBody ReservationDTO dto) {

        return ResponseEntity.ok(reservationService.createReservation(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.ok("Reservation cancelled successfully");
    }
}