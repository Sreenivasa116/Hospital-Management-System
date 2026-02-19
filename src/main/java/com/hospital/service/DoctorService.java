package com.hospital.service;

import com.hospital.entity.Doctor;
import com.hospital.enums.Role;
import com.hospital.exception.DoctorAlreadyExists;
import com.hospital.exception.DoctorNotFoundException;
import com.hospital.mapper.DoctorMapper;
import com.hospital.repository.DoctorProfessionalDetailsRepo;
import com.hospital.repository.DoctorRepository;
import com.hospital.requestDto.AddAvailabilityRequest;
import com.hospital.requestDto.DoctorRegisterRequest;
import com.hospital.requestDto.LoginRequest;
import com.hospital.responseDto.AvailabilityResponse;
import com.hospital.responseDto.DoctorRegisterResponse;
import com.hospital.responseDto.DoctorResponse;
import com.hospital.responseDto.LoginResponse;
import com.hospital.security.CustomUserDetails;
import com.hospital.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
public class DoctorService {

    private final PasswordEncoder passwordEncoder;
    private final DoctorMapper doctorMapper;
    private final DoctorRepository doctorRepository;
    private final DoctorProfessionalDetailsRepo doctorDetailsRepo;
    private final AvailabilityService availabilityService;

    public DoctorService(PasswordEncoder passwordEncoder,DoctorMapper doctorMapper, DoctorRepository doctorRepository,
                         DoctorProfessionalDetailsRepo doctorDetailsRepo, AvailabilityService availabilityService) {
        this.doctorDetailsRepo = doctorDetailsRepo;
        this.passwordEncoder = passwordEncoder;
        this.doctorMapper = doctorMapper;
        this.doctorRepository = doctorRepository;
        this.availabilityService = availabilityService;
    }


    @Transactional
    public DoctorRegisterResponse registerDoctor(DoctorRegisterRequest request) {

        Doctor doctor ;
        if (doctorRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DoctorAlreadyExists("Email already exists");
        } else {
            doctor = doctorMapper.toEntity(request);
            doctor.setPassword(passwordEncoder.encode(request.getPassword()));
            doctor = doctorRepository.save(doctor);

            String token = JwtUtil.generateToken( new CustomUserDetails(doctor.getId(),doctor.getEmail()
                    ,doctor.getPassword(), Role.DOCTOR));
            DoctorRegisterResponse response = new DoctorRegisterResponse(doctor.getId(), token);
            return response;
        }
    }

    @Transactional(readOnly = true)
    public LoginResponse authenticateDoctor(LoginRequest request) {

        Doctor doctor = doctorRepository.findByEmail(request.getEmail()).orElse(null);
        if (doctor != null && passwordEncoder.matches(request.getPassword(), doctor.getPassword())) {
                String token = JwtUtil.generateToken(new CustomUserDetails(doctor.getId(), doctor.getEmail()
                        ,doctor.getPassword(),Role.DOCTOR));
            return new LoginResponse(token, Role.DOCTOR);
        }else {
            throw new DoctorNotFoundException("Invalid Doctor credentials.Doctor Does not exists.");
        }
    }

    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));
        DoctorResponse response = doctorMapper.toRespone(doctor);
        Integer availableSlots = availabilityService.getAvailableSlots(id);
        response.setAvailableSlots(availableSlots);
        return response;
    }

    @Transactional
    public void addDoctorAvailability(Long doctorId, AddAvailabilityRequest request){

        doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
        availabilityService.addAvailability(doctorId,request);
    }

    @Transactional(readOnly = true)
    public List<AvailabilityResponse> getDoctorAvailability(Long id, LocalDate date){
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));
        return availabilityService.getDoctorAvailability(id,date);
    }

}
