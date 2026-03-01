package com.oceanview.service;

import com.oceanview.dto.ReservationCalculationDTO;
import com.oceanview.dto.ReservationDTO;
import com.oceanview.exception.ApiException;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.repository.GuestRepository;
import com.oceanview.repository.ReservationRepository;
import com.oceanview.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  GuestRepository guestRepository,
                                  RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
    }

    // ===== Mapping =====

    private ReservationDTO mapToDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .guestId(reservation.getGuestId())
                .roomId(reservation.getRoomId())
                .checkIn(reservation.getCheckIn())
                .checkOut(reservation.getCheckOut())
                .totalBill(reservation.getTotalBill())
                .build();
    }

    // ===== Service Methods =====

    @Override
    public ReservationDTO createReservation(ReservationDTO dto) {

        // Validate Guest
        if (!guestRepository.existsById(dto.getGuestId())) {
            throw new ApiException("Guest not found.", HttpStatus.NOT_FOUND);
        }

        // Validate Room
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ApiException("Room not found.", HttpStatus.NOT_FOUND));

        // Validate Dates
        if (dto.getCheckOut().isBefore(dto.getCheckIn())
                || dto.getCheckOut().isEqual(dto.getCheckIn())) {
            throw new ApiException("Invalid date range.", HttpStatus.BAD_REQUEST);
        }

        // Check room availability
        boolean isBooked = !reservationRepository
                .findByRoomIdAndCheckOutAfterAndCheckInBefore(
                        dto.getRoomId(),
                        dto.getCheckIn(),
                        dto.getCheckOut()
                ).isEmpty();

        if (isBooked) {
            throw new ApiException("Room is already booked for selected dates.",
                    HttpStatus.BAD_REQUEST);
        }

        // Calculate bill
        long days = ChronoUnit.DAYS.between(dto.getCheckIn(), dto.getCheckOut());
        BigDecimal totalBill = room.getPricePerNight()
                .multiply(BigDecimal.valueOf(days));

        Reservation reservation = Reservation.builder()
                .guestId(dto.getGuestId())
                .roomId(dto.getRoomId())
                .checkIn(dto.getCheckIn())
                .checkOut(dto.getCheckOut())
                .totalBill(totalBill)
                .createdBy(1)  // temporary until JWT
                .createdDate(LocalDateTime.now())
                .build();

        return mapToDTO(reservationRepository.save(reservation));
    }

    @Override
    public ReservationCalculationDTO calculateReservation(ReservationCalculationDTO dto) {

        // Validate Room
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ApiException("Room not found.", HttpStatus.NOT_FOUND));

        // Validate Dates
        if (dto.getCheckOut().isBefore(dto.getCheckIn())
                || dto.getCheckOut().isEqual(dto.getCheckIn())) {
            throw new ApiException("Invalid date range.", HttpStatus.BAD_REQUEST);
        }

        long days = ChronoUnit.DAYS.between(dto.getCheckIn(), dto.getCheckOut());

        BigDecimal totalAmount = room.getPricePerNight()
                .multiply(BigDecimal.valueOf(days));

        return ReservationCalculationDTO.builder()
                .roomId(dto.getRoomId())
                .checkIn(dto.getCheckIn())
                .checkOut(dto.getCheckOut())
                .days(days)
                .pricePerNight(room.getPricePerNight())
                .totalAmount(totalAmount)
                .build();
    }

    @Override
    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ApiException("Reservation not found.",
                        HttpStatus.NOT_FOUND));

        return mapToDTO(reservation);
    }

    @Override
    public void cancelReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}