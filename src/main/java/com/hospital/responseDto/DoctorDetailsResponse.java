package com.hospital.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class DoctorDetailsResponse {

    private Long doctorId;
    private String name;
    private String department;
    private String specialization;
    private BigDecimal feesPerConsult;
    private String clinicAddress;
    private Integer experienceYears;
    private Double rating;
//    private Long availableSlots;
}
