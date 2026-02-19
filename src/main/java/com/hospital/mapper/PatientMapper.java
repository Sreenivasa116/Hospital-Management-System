package com.hospital.mapper;


import com.hospital.entity.Patient;
import com.hospital.requestDto.PatientRegisterRequest;
import com.hospital.responseDto.PatientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(source = "name",target = "name")
    @Mapping(source = "age",target = "age")
    @Mapping(source = "email",target = "email")
    @Mapping(target = "password",ignore = true)
    @Mapping(source = "gender",target = "gender")
    @Mapping(source = "phone",target = "phone")
    @Mapping(source = "address",target = "address")
//    @Mapping(target = "bloodGroup",ignore = true)
//    @Mapping(target = "emergencyContact",ignore = true)
    Patient toEntity(PatientRegisterRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "bloodGroup", target = "bloodGroup")
    @Mapping(source = "emergencyContact", target = "emergencyContact")
    PatientResponse toDto(Patient patient);
    List<PatientResponse> toDtoList(List<Patient> patients);
}

