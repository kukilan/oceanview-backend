package com.oceanview.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO {

    private Long id;
    private Integer guestId;
    private Integer roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BigDecimal totalBill;
}