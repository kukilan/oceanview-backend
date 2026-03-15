package com.oceanview.service;

import com.oceanview.dto.DateRangeDTO;
import com.oceanview.dto.ReservationCalculationDTO;
import com.oceanview.dto.ReservationDTO;
import com.oceanview.dto.RoomDTO;

import java.util.List;

public interface ReservationService {

    ReservationDTO createReservation(ReservationDTO dto);

    ReservationCalculationDTO calculateReservation(ReservationCalculationDTO dto);

    List<ReservationDTO> getAllReservations();

    ReservationDTO getReservationById(Long id);

    List<RoomDTO> getAvailableRooms(DateRangeDTO dto);

    void cancelReservation(Long id);
    byte[] generateInvoice(Long id) throws Exception;
}