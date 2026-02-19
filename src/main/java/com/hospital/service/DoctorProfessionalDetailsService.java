package com.hospital.service;

import com.hospital.entity.DoctorProfessionalDetails;
import com.hospital.exception.DoctorNotFoundException;
import com.hospital.mapper.DoctorProfessionalDetailsMapper;
import com.hospital.repository.DoctorProfessionalDetailsRepo;
import com.hospital.requestDto.DoctorProfileRequest;
import com.hospital.responseDto.DoctorDetailsResponse;
import com.hospital.jpaSpecifications.DoctorProfessionalDetailsSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class DoctorProfessionalDetailsService {

    private final DoctorProfessionalDetailsRepo doctorDetailsRepo;
    private final DoctorProfessionalDetailsMapper doctorProfessionalDetailsMapper;
    public DoctorProfessionalDetailsService(DoctorProfessionalDetailsRepo doctorDetailsRepo,
                                            DoctorProfessionalDetailsMapper doctorProfessionalDetailsMapper) {
        this.doctorProfessionalDetailsMapper = doctorProfessionalDetailsMapper;
        this.doctorDetailsRepo = doctorDetailsRepo;
    }


    public Page<DoctorDetailsResponse> getDoctors(String department,BigDecimal feesPerConsultation,
                                                  String city,int pageNumber,int pageSize) {
        if (department != null || feesPerConsultation != null || city != null) {
            return getDoctorsByDetails(department, feesPerConsultation, city, pageNumber, pageSize);
        } else {
            return getDoctorsByPageAndSize(pageNumber, pageSize);
        }
    }

    public Page<DoctorDetailsResponse> getDoctorsByPageAndSize(int page, int size) {

        Page<DoctorDetailsResponse> doctors = doctorDetailsRepo.findAll(PageRequest.of(page, size))
                        .map(doctorProfessionalDetailsMapper::toDto);
        if(doctors.isEmpty() || doctors.getContent().isEmpty()){
            throw new DoctorNotFoundException("No doctors found");
        }
        return  doctors;
    }

    @Transactional(readOnly = true)
    public Page<DoctorDetailsResponse> getDoctorsByDetails
            (String department,BigDecimal feesPerConsult,String city, int page, int size) {

        Specification<DoctorProfessionalDetails> spec = Specification.where(null);

        if(department != null && !department.isEmpty()){
           spec = spec.and(DoctorProfessionalDetailsSpecification.hasDepartment(department));
        }else if(feesPerConsult != null){
           spec = spec.and(DoctorProfessionalDetailsSpecification.hasFeesPerConsult(feesPerConsult));
        }else if(city != null && !city.isEmpty()){
           spec = spec.and(DoctorProfessionalDetailsSpecification.hasCity(city));
        }

        Page<DoctorDetailsResponse> doctors = doctorDetailsRepo.findAll(spec, PageRequest.of(page, size))
                .map(doctorProfessionalDetailsMapper::toDto);
        if(doctors.isEmpty() || doctors.getContent().isEmpty()){
            throw new DoctorNotFoundException("No doctors found with the provided details");
        }
        return doctors;
    }


    //    public DoctorProfessionalDetails createDoctor(Long id,DoctorProfileRequest request) {
//
//
//        DoctorProfessionalDetails doctor = doctorProfessionalDetailsMapper.toEntity(request);
//        doctor.setId(id); // Temporary hardcoded value
//               doctor = doctorDetailsRepo.save(doctor);
//        return doctor;
//    }
}
