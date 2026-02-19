package com.hospital.requestDto;

import com.hospital.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientRegisterRequest {


    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Invalid Name format")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Invalid Age,Age must be at least 1")
    @Max(value = 120, message = "Invalid Age, must be less than 120")
    private Integer age;


    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Invalid phone number format"
    )
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email must be less than 150 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8,max = 14, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    private String password;

    @NotBlank(message = "Address is required")
    @Size(min = 10, max = 300, message = "Invalid Address format")
    private String address;

    @NotNull(message = "Gender is required")
    private Gender gender;

    private String bloodGroup;

    private String emergencyContact;
}
