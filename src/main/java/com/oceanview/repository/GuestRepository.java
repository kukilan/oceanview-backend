package com.oceanview.repository;

import com.oceanview.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Integer> {

    Optional<Guest> findByContactNumber(String contactNumber);

    Optional<Guest> findByEmail(String email);
}