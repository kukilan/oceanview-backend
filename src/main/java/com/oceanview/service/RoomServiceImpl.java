package com.oceanview.service;

import com.oceanview.dto.RoomDTO;
import com.oceanview.exception.ApiException;
import com.oceanview.model.Room;
import com.oceanview.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // ===== Mapping Methods =====

    private RoomDTO mapToDTO(Room room) {
        return RoomDTO.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .singleBeds(room.getSingleBeds())
                .doubleBeds(room.getDoubleBeds())
                .tripleBeds(room.getTripleBeds())
                .isAc(room.getIsAc())
                .pricePerNight(room.getPricePerNight())
                .build();
    }

    private Room mapToEntity(RoomDTO dto) {
        return Room.builder()
                .id(dto.getId())
                .roomNumber(dto.getRoomNumber())
                .singleBeds(dto.getSingleBeds())
                .doubleBeds(dto.getDoubleBeds())
                .tripleBeds(dto.getTripleBeds())
                .isAc(dto.getIsAc())
                .pricePerNight(dto.getPricePerNight())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // ===== Service Methods =====

    @Override
    public RoomDTO createRoom(RoomDTO roomDTO) {
        Room saved = roomRepository.save(mapToEntity(roomDTO));
        return mapToDTO(saved);
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTO getRoomById(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApiException("Room not found with ID: " + id, HttpStatus.NOT_FOUND));
        return mapToDTO(room);
    }

    @Override
    public RoomDTO updateRoom(Integer id, RoomDTO roomDTO) {

        Room existing = roomRepository.findById(id)
                .orElseThrow(() -> new ApiException("Room not found with ID: " + id, HttpStatus.NOT_FOUND));

        existing.setRoomNumber(roomDTO.getRoomNumber());
        existing.setSingleBeds(roomDTO.getSingleBeds());
        existing.setDoubleBeds(roomDTO.getDoubleBeds());
        existing.setTripleBeds(roomDTO.getTripleBeds());
        existing.setIsAc(roomDTO.getIsAc());
        existing.setPricePerNight(roomDTO.getPricePerNight());

        return mapToDTO(roomRepository.save(existing));
    }

    @Override
    public void deleteRoom(Integer id) {
        roomRepository.deleteById(id);
    }

    @Override
    public List<RoomDTO> getRoomsByAcType(Boolean isAc) {
        return roomRepository.findByIsAc(isAc)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> getRoomsByMaxPrice(BigDecimal price) {
        return roomRepository.findByPricePerNightLessThanEqual(price)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}