package com.oceanview.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_number", nullable = false, unique = true)
    private Integer roomNumber;

    @Column(name = "single_beds")
    private Integer singleBeds;

    @Column(name = "double_beds")
    private Integer doubleBeds;

    @Column(name = "triple_beds")
    private Integer tripleBeds;

    @Column(name = "is_ac")
    private Boolean isAc;

    @Column(name = "price_per_night", nullable = false)
    private BigDecimal pricePerNight;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

