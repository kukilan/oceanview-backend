package com.oceanview.controller;

import com.oceanview.dto.LoginRequestDTO;
import com.oceanview.model.UserProfile;
import com.oceanview.repository.UserProfileRepository;
import com.oceanview.security.JwtClaims;
import com.oceanview.security.JwtUtil;
import com.oceanview.repository.UserAuthRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserAuthRepository userAuthRepository;
    private final UserProfileRepository userProfileRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserAuthRepository userAuthRepository,
                          UserProfileRepository userProfileRepository,
                          BCryptPasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userAuthRepository = userAuthRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {

        var userAuth = userAuthRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), userAuth.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        var profile = userProfileRepository.findById(userAuth.getUserId())
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        JwtClaims claims = JwtClaims.builder()
                .username(userAuth.getUsername())
                .userId(profile.getId())
                .role(profile.getRole())
                .build();

        String token = jwtUtil.generateToken(claims);

        return ResponseEntity.ok(token);
    }
    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getProfile(
            @RequestHeader("Authorization") String header) {

        String token = header.substring(7); // Remove "Bearer "

        Integer userId = jwtUtil.extractUserId(token);

        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(profile);
    }
}