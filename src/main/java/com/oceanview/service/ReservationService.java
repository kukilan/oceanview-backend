package com.oceanview.service;

import com.oceanview.dto.ReservationCalculationDTO;
import com.oceanview.dto.ReservationDTO;

import java.util.List;

public interface ReservationService {

    ReservationDTO createReservation(ReservationDTO dto);

    ReservationCalculationDTO calculateReservation(ReservationCalculationDTO dto);

    List<ReservationDTO> getAllReservations();

    ReservationDTO getReservationById(Long id);

    void cancelReservation(Long id);
}