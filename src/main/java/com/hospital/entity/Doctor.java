package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "doctor",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone")
        },
        indexes = {
                @Index(name = "idx_doctor_email", columnList = "email"),
                @Index(name = "idx_doctor_phone", columnList = "phone")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name="id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "phone", length = 15, nullable = false)
    @ToString.Include
    private String phone;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private DoctorProfessionalDetails doctorProfile;

    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<DoctorAvailability> availability = new ArrayList<>();

}
