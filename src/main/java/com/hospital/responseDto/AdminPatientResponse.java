package com.hospital.responseDto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminPatientResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;


}
