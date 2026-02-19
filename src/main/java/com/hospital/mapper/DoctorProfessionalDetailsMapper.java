package com.hospital.mapper;

import com.hospital.entity.DoctorProfessionalDetails;
import com.hospital.requestDto.DoctorProfileRequest;
import com.hospital.responseDto.DoctorDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorProfessionalDetailsMapper {

    @Mapping(source = "id", target = "doctorId")
    @Mapping(source = "doctor.name", target = "name")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "specialization", target = "specialization")
    @Mapping(source = "feesPerConsult", target = "feesPerConsult")
    @Mapping(source = "clinicAddress", target = "clinicAddress")
    @Mapping(source = "experienceYears", target = "experienceYears")
    @Mapping(source = "rating", target = "rating")
//    @Mapping(source = "", target = "availableSlots")
    DoctorDetailsResponse toDto(DoctorProfessionalDetails entity);

    @Mapping(source = "position", target = "position")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "feesPerConsult", target = "feesPerConsult")
    @Mapping(source = "clinicAddress", target = "clinicAddress")
    @Mapping(source = "experienceYears", target = "experienceYears")
    @Mapping(source = "specialization", target = "specialization")
    @Mapping(source = "rating", target = "rating")
    DoctorProfessionalDetails toEntity(DoctorProfileRequest dto);

}
