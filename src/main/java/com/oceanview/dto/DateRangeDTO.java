package com.oceanview.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateRangeDTO {

    private LocalDate checkIn;
    private LocalDate checkOut;
}