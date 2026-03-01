package com.oceanview.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCalculationDTO {

    private Integer roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;

    // Response fields
    private Long days;
    private BigDecimal pricePerNight;
    private BigDecimal totalAmount;
}