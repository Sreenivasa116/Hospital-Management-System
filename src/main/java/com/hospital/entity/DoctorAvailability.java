package com.hospital.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Table(
        name = "availability",
        indexes = {
                @Index(name = "idx_doctor_date_time",columnList = "doctor_id,date,start_time,end_time")
        }
)
@Entity
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="doctor_id")
    private Doctor doctor;

    @Column(name="date",nullable = false)
    private LocalDate date;

    @Column(name="start_time",nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time",nullable = false)
    private LocalTime endTime;

    @Column(name = "duration_minutes",nullable = false)
    private Integer durationMinutes = 30;

    @Column(name="is_booked",nullable = false)
    private Boolean isBooked = false;
}
