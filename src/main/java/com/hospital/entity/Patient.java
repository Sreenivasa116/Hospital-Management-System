package com.hospital.entity;

import com.hospital.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "patient",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_patient_email", columnList = "email"),
                @Index(name = "idx_patient_phone", columnList = "phone")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name="id")
    private Long id;

    @ToString.Include
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ToString.Include
    @Column(name = "age")
    private Integer age;

    @ToString.Include
    @Column(name = "phone", length = 15,nullable = false)
    private String phone;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;


    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @ToString.Include
    @Column(name = "blood_group", length = 5)
    private String bloodGroup;

    @Column(name = "emergency_contact", length = 15)
    private String emergencyContact;
}
