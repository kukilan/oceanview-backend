package com.oceanview.service;

import com.oceanview.dto.*;

import java.util.List;
import java.util.Map;

public interface ReservationService {

    ReservationDTO createReservation(ReservationDTO dto);

    ReservationCalculationDTO calculateReservation(ReservationCalculationDTO dto);

    List<ReservationDTO> getAllReservations();

    ReservationDTO getReservationById(Long id);

    List<RoomDTO> getAvailableRooms(DateRangeDTO dto);

    void cancelReservation(Long id);
    byte[] generateInvoice(Long id) throws Exception;
    public Map<String, Object> getRoomStatus(Integer roomNumber);
    DashboardDTO getDashboardStats();
}