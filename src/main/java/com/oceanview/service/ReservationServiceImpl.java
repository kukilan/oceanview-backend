package com.oceanview.service;

import com.oceanview.dto.DateRangeDTO;
import com.oceanview.dto.ReservationCalculationDTO;
import com.oceanview.dto.ReservationDTO;
import com.oceanview.dto.RoomDTO;
import com.oceanview.exception.ApiException;
import com.oceanview.helper.PdfGeneratorHelper;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.repository.GuestRepository;
import com.oceanview.repository.ReservationRepository;
import com.oceanview.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private Integer getCurrentUserId() {
        return (Integer) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    // ===== Service Methods =====

    @Override
    public ReservationDTO createReservation(ReservationDTO dto) {

        if (!guestRepository.existsById(dto.getGuestId())) {
            throw new ApiException("Guest not found.", HttpStatus.NOT_FOUND);
        }

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ApiException("Room not found.", HttpStatus.NOT_FOUND));

        if (dto.getCheckOut().isBefore(dto.getCheckIn())
                || dto.getCheckOut().isEqual(dto.getCheckIn())) {
            throw new ApiException("Invalid date range.", HttpStatus.BAD_REQUEST);
        }

        boolean isBooked = !reservationRepository
                .findByRoomIdAndCheckOutAfterAndCheckInBefore(
                        dto.getRoomId(),
                        dto.getCheckIn(),
                        dto.getCheckOut()
                ).isEmpty();

        if (isBooked) {
            throw new ApiException(
                    "Room is already booked for selected dates.",
                    HttpStatus.BAD_REQUEST
            );
        }

        long days = ChronoUnit.DAYS.between(dto.getCheckIn(), dto.getCheckOut());

        BigDecimal totalBill = room.getPricePerNight()
                .multiply(BigDecimal.valueOf(days));

        Reservation reservation = Reservation.builder()
                .guestId(dto.getGuestId())
                .roomId(dto.getRoomId())
                .checkIn(dto.getCheckIn())
                .checkOut(dto.getCheckOut())
                .totalBill(totalBill)
                .createdBy(getCurrentUserId())
                .createdDate(LocalDateTime.now())
                .isDeleted(false)
                .build();

        reservationRepository.save(reservation);

        return mapToDTO(reservation);
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
        return reservationRepository.findByIsDeletedFalse()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> getAvailableRooms(DateRangeDTO dto) {

        if (dto.getCheckOut().isBefore(dto.getCheckIn())
                || dto.getCheckOut().isEqual(dto.getCheckIn())) {
            throw new ApiException("Invalid date range.", HttpStatus.BAD_REQUEST);
        }

        // Get all rooms
        List<Room> allRooms = roomRepository.findAll();

        // Get overlapping reservations
        List<Reservation> overlappingReservations =
                reservationRepository.findByCheckOutAfterAndCheckInBefore(
                        dto.getCheckIn(),
                        dto.getCheckOut()
                );

        // Extract booked room IDs
        List<Integer> bookedRoomIds = overlappingReservations.stream()
                .map(Reservation::getRoomId)
                .toList();

        // Filter available rooms
        return allRooms.stream()
                .filter(room -> !bookedRoomIds.contains(room.getId()))
                .map(room -> {
                    int totalBeds =
                            (room.getSingleBeds() != null ? room.getSingleBeds() : 0) +
                                    (room.getDoubleBeds() != null ? room.getDoubleBeds() : 0) +
                                    (room.getTripleBeds() != null ? room.getTripleBeds() : 0);

                    return RoomDTO.builder()
                            .id(room.getId())
                            .roomNumber(room.getRoomNumber())
                            .singleBeds(room.getSingleBeds())
                            .doubleBeds(room.getDoubleBeds())
                            .tripleBeds(room.getTripleBeds())
                            .isAc(room.getIsAc())
                            .pricePerNight(room.getPricePerNight())
                            .totalBeds(totalBeds)
                            .build();
                })
                .toList();
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

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException("Reservation not found.", HttpStatus.NOT_FOUND));

        reservation.setIsDeleted(true);
        reservation.setModifiedBy(getCurrentUserId());
        reservation.setModifiedDate(LocalDateTime.now());

        reservationRepository.save(reservation);
    }

    @Override
    public byte[] generateInvoice(Long id) throws Exception {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ApiException("Reservation not found.", HttpStatus.NOT_FOUND));

        Guest guest = guestRepository.findById(reservation.getGuestId())
                .orElseThrow(() -> new ApiException("Guest not found.", HttpStatus.NOT_FOUND));

        Room room = roomRepository.findById(reservation.getRoomId())
                .orElseThrow(() -> new ApiException("Room not found.", HttpStatus.NOT_FOUND));

        return PdfGeneratorHelper.generateReservationPdf(
                id.toString(),
                guest.getFullName(),
                room.getRoomNumber(),
                reservation.getCheckIn().toString(),
                reservation.getCheckOut().toString(),
                reservation.getTotalBill()
        );
    }
}
