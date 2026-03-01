package com.oceanview.service;

import com.oceanview.model.Room;
import com.oceanview.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(Room room) {
        room.setCreatedAt(LocalDateTime.now());
        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(Integer id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + id));
    }

    @Override
    public Room updateRoom(Integer id, Room updatedRoom) {

        Room existingRoom = getRoomById(id);

        existingRoom.setRoomNumber(updatedRoom.getRoomNumber());
        existingRoom.setSingleBeds(updatedRoom.getSingleBeds());
        existingRoom.setDoubleBeds(updatedRoom.getDoubleBeds());
        existingRoom.setTripleBeds(updatedRoom.getTripleBeds());
        existingRoom.setIsAc(updatedRoom.getIsAc());
        existingRoom.setPricePerNight(updatedRoom.getPricePerNight());

        return roomRepository.save(existingRoom);
    }

    @Override
    public void deleteRoom(Integer id) {
        roomRepository.deleteById(id);
    }

    @Override
    public List<Room> getRoomsByAcType(Boolean isAc) {
        return roomRepository.findByIsAc(isAc);
    }

    @Override
    public List<Room> getRoomsByMaxPrice(BigDecimal price) {
        return roomRepository.findByPricePerNightLessThanEqual(price);
    }
}