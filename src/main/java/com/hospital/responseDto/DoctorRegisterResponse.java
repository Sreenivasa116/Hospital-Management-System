package com.hospital.responseDto;

import lombok.*;


@Getter
@NoArgsConstructor


public class DoctorRegisterResponse {

    private Long patientId;
    private String jwtToken;

    public DoctorRegisterResponse(Long patientId, String jwtToken) {
        this.patientId = patientId;
        this.jwtToken = jwtToken;
    };

}
