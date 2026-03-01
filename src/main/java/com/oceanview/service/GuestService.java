package com.oceanview.service;

import com.oceanview.dto.GuestDTO;

import java.util.List;

public interface GuestService {

    GuestDTO createGuest(GuestDTO guestDTO);

    List<GuestDTO> getAllGuests();

    GuestDTO getGuestById(Integer id);

    GuestDTO updateGuest(Integer id, GuestDTO guestDTO);

    void deleteGuest(Integer id);
}