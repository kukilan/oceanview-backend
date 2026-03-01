package com.oceanview.controller;

import com.oceanview.dto.GuestDTO;
import com.oceanview.service.GuestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping("/create")
    public ResponseEntity<GuestDTO> createGuest(@RequestBody GuestDTO guestDTO) {
        return ResponseEntity.ok(guestService.createGuest(guestDTO));
    }

    @GetMapping
    public ResponseEntity<List<GuestDTO>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestDTO> getGuestById(@PathVariable Integer id) {
        return ResponseEntity.ok(guestService.getGuestById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuestDTO> updateGuest(
            @PathVariable Integer id,
            @RequestBody GuestDTO guestDTO) {
        return ResponseEntity.ok(guestService.updateGuest(id, guestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGuest(@PathVariable Integer id) {
        guestService.deleteGuest(id);
        return ResponseEntity.ok("Guest deleted successfully");
    }
}