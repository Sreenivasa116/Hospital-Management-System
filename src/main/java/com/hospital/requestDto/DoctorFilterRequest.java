package com.hospital.requestDto;


import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DoctorFilterRequest {

    @Size(max = 100)
    private String department;
    @Positive
    private Integer feesLt;
    @Size(max=100)
    private String city;

    // getters & setters
}

