package com.hospital.responseDto;

import com.hospital.enums.Gender;
import lombok.*;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class PatientResponse {

    private Long id;
    private String name;
    private Integer age;
    private String phone;
    private String email;
    private String address;
    private Gender gender;
    private String bloodGroup;
    private String emergencyContact;

}

