package com.hospital.controller;


import com.hospital.enums.Role;
import com.hospital.exception.LoginFailedException;
import com.hospital.requestDto.*;
import com.hospital.responseDto.DoctorRegisterResponse;
import com.hospital.responseDto.LoginResponse;
import com.hospital.responseDto.PatientRegisterResponse;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Validated
public class MainController  {

    private final PatientService patientService;
    private final DoctorService doctorService;

    public MainController(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @PostMapping("/patient/register")
    public ResponseEntity<PatientRegisterResponse> registerPatient
            ( @Valid @RequestBody PatientRegisterRequest request){

        PatientRegisterResponse response = patientService.registerPatient(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/doctor/register")
    public ResponseEntity<DoctorRegisterResponse> registerDoctor
            ( @Valid @RequestBody DoctorRegisterRequest request) {
        DoctorRegisterResponse response = doctorService.registerDoctor(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login
            ( @Valid @RequestBody LoginRequest request) {

        LoginResponse response ;
        if(request.getRole() == Role.PATIENT){

            response = patientService.authenticatePatient(request);
            return ResponseEntity.status(201).body(response);

        }else if(request.getRole() == Role.DOCTOR) {

            response = doctorService.authenticateDoctor(request);
            return ResponseEntity.status(201).body(response);
        }
        else
            throw new LoginFailedException("Please provide valid role");

    }

}