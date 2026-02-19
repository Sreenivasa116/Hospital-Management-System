package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "admin",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_admin_email", columnList = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;
}
