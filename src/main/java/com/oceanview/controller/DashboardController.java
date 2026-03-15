package com.oceanview.controller;

import com.oceanview.dto.DashboardDTO;
import com.oceanview.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ReservationService reservationService;

    public DashboardController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardDTO> getStats() {

        return ResponseEntity.ok(reservationService.getDashboardStats());

    }
}
