package com.hospital.requestDto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class DoctorProfileRequest {

    @NotBlank
    private String position;
    @NotBlank
    private String department;
    @NotNull
    private BigDecimal feesPerConsult;
    @Size(min=3)
    private String clinicAddress;
    private Integer experienceYears;

    @Size(max=100)
    private String specialization;
    @DecimalMin(value="0.0", inclusive = true)
    @DecimalMax(value = "5.0", inclusive = true)
    private  Double rating;

}

