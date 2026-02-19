package com.hospital.repository;

import com.hospital.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.List;

@Repository
public interface DoctorAvailabilityRepo extends JpaRepository<DoctorAvailability,Long> {

    Integer countByDoctorId(Long doctorId);

    @Query("""
        SELECT COUNT(a) > 0
        FROM DoctorAvailability a
        WHERE a.doctor.id = :doctorId
          AND a.date = :date
          AND :startTime < a.endTime
          AND :endTime > a.startTime
    """)
    boolean existsOverlappingSlot(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
    List<DoctorAvailability> findAllByDoctorIdAndDate
            (Long doctorId, LocalDate date);
}
