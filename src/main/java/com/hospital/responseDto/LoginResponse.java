package com.hospital.responseDto;

import com.hospital.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
public class LoginResponse {

    private String jwtToken;
    private Role role;

    public LoginResponse(String jwtToken, Role role) {
        this.jwtToken = jwtToken;
        this.role = role;
    }
}
