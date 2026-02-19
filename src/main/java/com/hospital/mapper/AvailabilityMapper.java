package com.hospital.mapper;

import com.hospital.entity.DoctorAvailability;
import com.hospital.requestDto.AddAvailabilityRequest;
import com.hospital.responseDto.AvailabilityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AvailabilityMapper {

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "durationMinutes",ignore = true)
    @Mapping(target = "isBooked",ignore = true)
    DoctorAvailability toEntity(LocalDate date,LocalTime startTime,LocalTime endTime);

    @Mapping(source = "startTime",target = "timeSlot")
    @Mapping(source = "isBooked",target = "isBooked")
    AvailabilityResponse toResponse(DoctorAvailability availability);
    List<AvailabilityResponse> toResponseList(List<DoctorAvailability> availabilities);
}
