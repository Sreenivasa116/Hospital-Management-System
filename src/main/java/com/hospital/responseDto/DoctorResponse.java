package com.hospital.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class DoctorResponse {

    private Long id;
    private String name;
//    private String email;
//    private String phoneNumber;
    private String position;
    private String department;
    private String specialization;
    private BigDecimal feesPerConsult;
    private String clinicAddress;
    private Integer experienceYears;
    private Double rating;
    private Integer availableSlots;

}
