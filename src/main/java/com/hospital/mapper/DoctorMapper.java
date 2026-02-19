package com.hospital.mapper;

import com.hospital.entity.Doctor;
import com.hospital.requestDto.DoctorRegisterRequest;
import com.hospital.responseDto.DoctorDetailsResponse;
import com.hospital.responseDto.DoctorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(source = "name",target = "name")
    @Mapping(source = "phone",target = "phone")
    @Mapping(source = "email",target = "email")
    @Mapping(target = "password", ignore = true)
    Doctor toEntity(DoctorRegisterRequest dto);

    @Mapping(source = "id",target = "id" )
    @Mapping(source = "name",target = "name" )
    @Mapping(source = "doctorProfile.position",target = "position" )
    @Mapping(source = "doctorProfile.department",target = "department" )
    @Mapping(source = "doctorProfile.feesPerConsult",target = "feesPerConsult" )
    @Mapping(source = "doctorProfile.clinicAddress",target = "clinicAddress" )
    @Mapping(source = "doctorProfile.experienceYears",target = "experienceYears" )
    @Mapping(source = "doctorProfile.specialization",target = "specialization" )
    @Mapping(source = "doctorProfile.rating",target = "rating" )
//    @Mapping(target = "availableSlots",ignore = true)
    DoctorResponse toRespone(Doctor doctor);
}
