package com.altamirano_pajares.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

// Caso 1 - Docente (Teacher) - ALTAMIRANO SANCHEZ JEAN PIERRE
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "teachers")
@Check(name = "CK_TEACHER_EMAIL", constraints = "email LIKE '%_@_%._%'") // formato de email a nivel de BD
public class Teacher {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTeacher;

    @Column(nullable = false, unique = true, length = 20)
    private String teacherCode;

    @Column(nullable = false, length = 60)
    private String firstName;

    @Column(nullable = false, length = 60)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING) // guarda "MAGISTER" y no el ordinal 1
    @Column(nullable = false, length = 15)
    private AcademicDegree academicDegree;
}
