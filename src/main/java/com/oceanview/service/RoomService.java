package com.oceanview.service;

import com.oceanview.dto.RoomDTO;

import java.math.BigDecimal;
import java.util.List;

public interface RoomService {

    RoomDTO createRoom(RoomDTO roomDTO);

    List<RoomDTO> getAllRooms();

    RoomDTO getRoomById(Integer id);

    RoomDTO updateRoom(Integer id, RoomDTO roomDTO);

    void deleteRoom(Integer id);

    List<RoomDTO> getRoomsByAcType(Boolean isAc);

    List<RoomDTO> getRoomsByMaxPrice(BigDecimal price);
}