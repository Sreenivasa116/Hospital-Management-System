package com.hospital.controller;



import com.hospital.entity.DoctorAvailability;
import com.hospital.entity.DoctorProfessionalDetails;
import com.hospital.requestDto.AddAvailabilityRequest;
import com.hospital.requestDto.DoctorProfileRequest;
import com.hospital.responseDto.AvailabilityResponse;
import com.hospital.responseDto.DoctorDetailsResponse;
import com.hospital.responseDto.DoctorResponse;
import com.hospital.service.DoctorProfessionalDetailsService;
import com.hospital.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@Validated
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorProfessionalDetailsService doctorProfessionalDetailsService;


    public DoctorController(DoctorService doctorService,
                            DoctorProfessionalDetailsService detailsService) {
        this.doctorProfessionalDetailsService = detailsService;
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<Page<DoctorDetailsResponse>>getAllDoctors
            (@RequestParam(required = false) String department,
              @RequestParam(required = false) BigDecimal feesPerConsultation,
              @RequestParam(required = false)String city,
              @RequestParam(defaultValue = "0") int pageNumber,
              @RequestParam(defaultValue = "5") int pagesize){
            Page<DoctorDetailsResponse> doctors = doctorProfessionalDetailsService.getDoctors
                    (department,feesPerConsultation,city,pageNumber,pagesize);

        return ResponseEntity.status(HttpStatus.OK).body(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id){
       DoctorResponse doctor = doctorService.getDoctorById(id);
         return ResponseEntity.ok(doctor);
    }

    @PostMapping("/{id}/availability")
    public ResponseEntity<String> setDoctorAvailability(@RequestBody AddAvailabilityRequest request,@PathVariable Long id){

        doctorService.addDoctorAvailability(id,request);
        return ResponseEntity.ok("Availability added successfully");
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<List<AvailabilityResponse>> getDoctorAvailability
            (@PathVariable Long id,@RequestParam LocalDate date){
            List<AvailabilityResponse> availability = doctorService.getDoctorAvailability(id,date);
            return ResponseEntity.ok(availability);
    }

    //    @PostMapping("/profile/{id}")
//    public ResponseEntity<DoctorProfessionalDetails> createDoctorProfessionalDetails
//            (@Valid @RequestBody DoctorProfileRequest request,@RequestParam Long id){
//        DoctorProfessionalDetails doctor  = doctorProfessionalDetailsService.createDoctor(id,request);
//        return ResponseEntity.ok(doctor);
//    }

}
