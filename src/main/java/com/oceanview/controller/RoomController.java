package com.oceanview.controller;

import com.oceanview.model.Room;
import com.oceanview.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Create Room
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return ResponseEntity.ok(roomService.createRoom(room));
    }

    // Get All Rooms
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // Get Room By ID
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Integer id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // Update Room
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable Integer id,
            @RequestBody Room room) {
        return ResponseEntity.ok(roomService.updateRoom(id, room));
    }

    // Delete Room
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }

    // Filter by AC type
    @GetMapping("/filter/ac")
    public ResponseEntity<List<Room>> getRoomsByAcType(@RequestParam Boolean isAc) {
        return ResponseEntity.ok(roomService.getRoomsByAcType(isAc));
    }

    // Filter by max price
    @GetMapping("/filter/price")
    public ResponseEntity<List<Room>> getRoomsByMaxPrice(@RequestParam BigDecimal price) {
        return ResponseEntity.ok(roomService.getRoomsByMaxPrice(price));
    }
}