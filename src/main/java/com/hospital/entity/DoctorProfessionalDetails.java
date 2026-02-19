package com.hospital.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "doctor_professional_details",

        indexes = {
                @Index(name = "idx_doctor_department", columnList = "department"),
                @Index(name = "idx_doctor_specialization", columnList = "specialization"),

        }

)
@Setter
@Getter
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class DoctorProfessionalDetails {

    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private Doctor doctor;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "position",nullable = false)
    private String position;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "department",nullable = false)
    private String department;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name="fees_per_consult",nullable = false)
    private BigDecimal feesPerConsult;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "clinic_address",nullable = false)
    private String clinicAddress;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "experience_years",nullable = false)
    private Integer experienceYears;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "rating",nullable = false)
    private Double rating;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "specialization",nullable = false)
    private String specialization;

}
