package com.oceanview.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {

    private Integer id;
    private Integer roomNumber;
    private Integer singleBeds;
    private Integer doubleBeds;
    private Integer tripleBeds;
    private Boolean isAc;
    private BigDecimal pricePerNight;
}