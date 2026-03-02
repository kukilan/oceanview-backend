package com.oceanview.repository;

import com.oceanview.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Integer> {

    Optional<UserAuth> findByUsername(String username);
}