package com.oceanview.service;

import com.oceanview.dto.GuestDTO;
import com.oceanview.exception.ApiException;
import com.oceanview.model.Guest;
import com.oceanview.repository.GuestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    public GuestServiceImpl(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    // ===== Mapping Methods =====

    private GuestDTO mapToDTO(Guest guest) {
        return GuestDTO.builder()
                .id(guest.getId())
                .fullName(guest.getFullName())
                .address(guest.getAddress())
                .contactNumber(guest.getContactNumber())
                .email(guest.getEmail())
                .gender(guest.getGender())
                .dateOfBirth(guest.getDateOfBirth())
                .build();
    }

    private Guest mapToEntity(GuestDTO dto) {
        return Guest.builder()
                .id(dto.getId())
                .fullName(dto.getFullName())
                .address(dto.getAddress())
                .contactNumber(dto.getContactNumber())
                .email(dto.getEmail())
                .gender(dto.getGender())
                .dateOfBirth(dto.getDateOfBirth())
                .createdAt(LocalDateTime.now())
                .createdBy(getCurrentUserId())
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
    public GuestDTO createGuest(GuestDTO guestDTO) {

        if (guestRepository.findByContactNumber(guestDTO.getContactNumber()).isPresent()) {
            throw new ApiException("Contact number already exists.", HttpStatus.BAD_REQUEST);
        }

        if (guestDTO.getEmail() != null &&
                guestRepository.findByEmail(guestDTO.getEmail()).isPresent()) {
            throw new ApiException("Email already exists.", HttpStatus.BAD_REQUEST);
        }

        Guest saved = guestRepository.save(mapToEntity(guestDTO));
        return mapToDTO(saved);
    }

    @Override
    public List<GuestDTO> getAllGuests() {
        return guestRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GuestDTO getGuestById(Integer id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ApiException("Guest not found with ID: " + id, HttpStatus.NOT_FOUND));
        return mapToDTO(guest);
    }

    @Override
    public GuestDTO updateGuest(Integer id, GuestDTO guestDTO) {

        Guest existing = guestRepository.findById(id)
                .orElseThrow(() -> new ApiException("Guest not found with ID: " + id, HttpStatus.NOT_FOUND));

        existing.setFullName(guestDTO.getFullName());
        existing.setAddress(guestDTO.getAddress());
        existing.setContactNumber(guestDTO.getContactNumber());
        existing.setEmail(guestDTO.getEmail());
        existing.setGender(guestDTO.getGender());
        existing.setDateOfBirth(guestDTO.getDateOfBirth());

        return mapToDTO(guestRepository.save(existing));
    }

    @Override
    public void deleteGuest(Integer id) {
        guestRepository.deleteById(id);
    }
}