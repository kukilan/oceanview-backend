package com.oceanview.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestDTO {

    private Integer id;
    private String fullName;
    private String address;
    private String contactNumber;
    private String email;
    private String gender;
    private LocalDate dateOfBirth;
}