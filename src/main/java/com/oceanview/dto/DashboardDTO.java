package com.oceanview.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {

    private Long totalRooms;
    private Long activeReservations;
    private BigDecimal todayRevenue;

}