package com.hospital.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PatientRegisterResponse {

    private Long patientId;
    private String jwtToken;

    public PatientRegisterResponse(Long patientId, String jwtToken) {
        this.patientId = patientId;
        this.jwtToken = jwtToken;
    }

}




