package com.altamirano_pajares.repository;

import com.altamirano_pajares.model.Teacher;

public interface ITeacherRepository extends IGenericRepository<Teacher, Integer> {
    // Query methods: Spring Data genera el SQL a partir del nombre del metodo
    boolean existsByTeacherCodeIgnoreCase(String teacherCode);
    boolean existsByEmailIgnoreCase(String email);

    // Para el PUT: el valor unico solo puede repetirse en el mismo registro
    boolean existsByTeacherCodeIgnoreCaseAndIdTeacherNot(String teacherCode, Integer idTeacher);
    boolean existsByEmailIgnoreCaseAndIdTeacherNot(String email, Integer idTeacher);
}
