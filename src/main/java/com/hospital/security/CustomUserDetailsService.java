
package com.hospital.security;

import com.hospital.entity.Admin;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.enums.Role;
import com.hospital.repository.AdminRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AdminRepository adminRepository;

    public CustomUserDetailsService(PatientRepository patientRepository,
                                    DoctorRepository doctorRepository,
                                    AdminRepository adminRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Patient patient = patientRepository.findByEmail(email).orElse(null);
        if (patient != null) {
            return new CustomUserDetails(
                    patient.getId(),
                    patient.getEmail(),
                    patient.getPassword(),
                    Role.PATIENT
            );
        }

        Doctor doctor = doctorRepository.findByEmail(email).orElse(null);
        if (doctor != null) {
            return new CustomUserDetails(
                    doctor.getId(),
                    doctor.getEmail(),
                    doctor.getPassword(),
                    Role.DOCTOR
            );
        }

        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            return new CustomUserDetails(
                    admin.getId(),
                    admin.getEmail(),
                    admin.getPassword(),
                    Role.ADMIN
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}

