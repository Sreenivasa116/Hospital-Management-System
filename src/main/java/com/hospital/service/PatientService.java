package com.hospital.service;

import com.hospital.entity.*;
import com.hospital.enums.Role;
import com.hospital.exception.PatientAlreadyExistsByEmail;
import com.hospital.exception.PatientNotFoundException;
import com.hospital.mapper.PatientMapper;
import com.hospital.repository.PatientRepository;
import com.hospital.requestDto.*;
import com.hospital.responseDto.*;

import com.hospital.security.CustomUserDetails;
import com.hospital.security.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepository, PasswordEncoder passwordEncoder,
                          PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.patientMapper = patientMapper;
    }

    @Transactional
    public PatientRegisterResponse registerPatient(PatientRegisterRequest request){

        String email = request.getEmail();
        Patient patient ;
        if( patientRepository.findByEmail(email).isPresent())
            throw new PatientAlreadyExistsByEmail("Patient already exists.Please login instead");
        else
            patient = patientMapper.toEntity(request);
            patient.setPassword(passwordEncoder.encode(request.getPassword()));
            patient = patientRepository.save(patient);
            CustomUserDetails userDetails = new CustomUserDetails(patient.getId(),patient.getEmail(), patient.getPassword(), Role.PATIENT);
            String token = JwtUtil.generateToken(userDetails);
            PatientRegisterResponse response = new PatientRegisterResponse(patient.getId(), token);
         return response;
    }

    public LoginResponse authenticatePatient(LoginRequest request) {

        Patient patient = patientRepository.findByEmail(request.getEmail()).orElse(null);
        if (patient != null && passwordEncoder.matches(request.getPassword(), patient.getPassword())) {

            String token = JwtUtil.generateToken(new CustomUserDetails(patient.getId(), patient.getEmail()
                    ,patient.getPassword(),Role.PATIENT));
            return new LoginResponse(token,Role.PATIENT);
        }else
            throw new PatientNotFoundException("Invalid details.Patient does not exists.");
    }

    public Page<PatientResponse> getAllPatients(int page,int size,String city) {

        Page<PatientResponse> response;
         if(city != null && !city.isEmpty())
             response = getPatientsByCity(page,size,city);
         else
             response =getPatients(page,size);

         if(response.isEmpty())
            throw new PatientNotFoundException("No patients found.");

         return response;
    }

    public Page<PatientResponse> getPatients(int page, int size) {
        Page<Patient> patientPage = patientRepository.findAll(PageRequest.of(page,size));
        Page<PatientResponse> responsePage = patientPage.map(patientMapper::toDto);
        return responsePage;
    }

    public Page<PatientResponse> getPatientsByCity(int page, int size,String city) {
        Page<Patient> patientPage = patientRepository.findByAddressContainingIgnoreCase(city,PageRequest.of(page,size));
        Page<PatientResponse> responsePage = patientPage.map(patientMapper::toDto);
        return responsePage;
    }

}
