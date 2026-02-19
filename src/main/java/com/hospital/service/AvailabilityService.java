package com.hospital.service;

import com.hospital.entity.Doctor;
import com.hospital.entity.DoctorAvailability;
import com.hospital.exception.DoctorNotAvailableException;
import com.hospital.exception.TimeFormatException;
import com.hospital.mapper.AvailabilityMapper;
import com.hospital.repository.DoctorAvailabilityRepo;
import com.hospital.requestDto.AddAvailabilityRequest;
import com.hospital.responseDto.AvailabilityResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class AvailabilityService {

    private  final DoctorAvailabilityRepo doctorAvailabilityRepo;
    private final AvailabilityMapper availabilityMapper;

    public AvailabilityService(DoctorAvailabilityRepo doctorAvailabilityRepo, AvailabilityMapper availabilityMapper) {
        this.doctorAvailabilityRepo = doctorAvailabilityRepo;
        this.availabilityMapper = availabilityMapper;
    }

    @Transactional(readOnly = true)
    public  Integer getAvailableSlots(Long doctorId) {
        int result = 0;
         result = doctorAvailabilityRepo.countByDoctorId(doctorId);
        if(result == 0)
            throw new DoctorNotAvailableException("Doctor is not available");
        return result;
    }

    @Transactional
    public void addAvailability(Long doctorId, AddAvailabilityRequest request) {

        LocalDate date = request.getDate();
        LocalTime startTime = request.getStartTime();
        LocalTime endTime = request.getEndTime();

        if (!startTime.isBefore(endTime)) {
            throw new TimeFormatException("Start time must be before end time");
        }
        int duration = 30;
        long totalMinutes = java.time.Duration
                .between(startTime, endTime)
                .toMinutes();

        if (totalMinutes % duration != 0) {
            throw new TimeFormatException("Time range must align with 30-minute slots");
        }

        boolean exists = doctorAvailabilityRepo.existsOverlappingSlot(
                doctorId, date,startTime,endTime);

        if (exists) {
            throw new TimeFormatException("Time range overlaps with existing availability");
        }

        List<DoctorAvailability> slots = new ArrayList<>();

        LocalTime currentStart = startTime;

        while (currentStart.isBefore(endTime)) {

            LocalTime slotEnd = currentStart.plusMinutes(duration);
            DoctorAvailability availability = availabilityMapper.toEntity(date, currentStart, slotEnd);
            availability.setDoctor(new Doctor());
            availability.getDoctor().setId(doctorId);
            slots.add(availability);
            currentStart = slotEnd;
        }
        doctorAvailabilityRepo.saveAll(slots);
    }

    @Transactional(readOnly = true)
    public List<AvailabilityResponse>  getDoctorAvailability(Long id,LocalDate date){

       List<AvailabilityResponse > availabity = availabilityMapper.
               toResponseList(doctorAvailabilityRepo.findAllByDoctorIdAndDate(id,date));
       if(availabity.isEmpty())
           throw new DoctorNotAvailableException("Doctor is not available on the given date");

       return availabity;
    }

}
