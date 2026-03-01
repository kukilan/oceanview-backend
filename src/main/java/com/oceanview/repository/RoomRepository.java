package com.oceanview.repository;

import com.oceanview.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    // Find by room number
    Optional<Room> findByRoomNumber(Integer roomNumber);

    // Find rooms by AC type
    List<Room> findByIsAc(Boolean isAc);

    // Find rooms cheaper than given price
    List<Room> findByPricePerNightLessThanEqual(java.math.BigDecimal price);

}