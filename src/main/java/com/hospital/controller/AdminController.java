package com.hospital.controller;

import com.hospital.responseDto.PatientResponse;
import com.hospital.service.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final PatientService patientService;

    public AdminController(PatientService patientService){

        this.patientService = patientService;
    }


    @GetMapping("/patients")
    public ResponseEntity<Page<PatientResponse>> getPatients(@RequestParam int page, @RequestParam int size,
                                                            @RequestParam(required = false) String city) {
        Page<PatientResponse> patients = patientService.getAllPatients(page,size,city);
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

}
