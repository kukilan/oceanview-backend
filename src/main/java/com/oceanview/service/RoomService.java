package com.oceanview.service;

import com.oceanview.model.Room;
import java.math.BigDecimal;
import java.util.List;

public interface RoomService {

    Room createRoom(Room room);

    List<Room> getAllRooms();

    Room getRoomById(Integer id);

    Room updateRoom(Integer id, Room room);

    void deleteRoom(Integer id);

    List<Room> getRoomsByAcType(Boolean isAc);

    List<Room> getRoomsByMaxPrice(BigDecimal price);
}
