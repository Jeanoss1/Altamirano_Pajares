package com.altamirano_pajares.service.implementation;

import com.altamirano_pajares.exception.BusinessRuleException;
import com.altamirano_pajares.model.Teacher;
import com.altamirano_pajares.repository.IGenericRepository;
import com.altamirano_pajares.repository.ITeacherRepository;
import com.altamirano_pajares.service.ITeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService extends GenericService<Teacher, Integer> implements ITeacherService {
    private final ITeacherRepository repo;

    @Override
    protected IGenericRepository<Teacher, Integer> getRepo() {
        return repo;
    }

    @Override
    public Teacher save(Teacher teacher) {
        normalize(teacher);
        if (repo.existsByTeacherCodeIgnoreCase(teacher.getTeacherCode())) {
            throw new BusinessRuleException("Ya existe un docente con el teacherCode " + teacher.getTeacherCode());
        }
        if (repo.existsByEmailIgnoreCase(teacher.getEmail())) {
            throw new BusinessRuleException("Ya existe un docente con el email " + teacher.getEmail());
        }
        return super.save(teacher);
    }

    @Override
    public Teacher update(Integer id, Teacher teacher) {
        findById(id); // primero 404, luego reglas de negocio
        normalize(teacher);
        if (repo.existsByTeacherCodeIgnoreCaseAndIdTeacherNot(teacher.getTeacherCode(), id)) {
            throw new BusinessRuleException("Ya existe otro docente con el teacherCode " + teacher.getTeacherCode());
        }
        if (repo.existsByEmailIgnoreCaseAndIdTeacherNot(teacher.getEmail(), id)) {
            throw new BusinessRuleException("Ya existe otro docente con el email " + teacher.getEmail());
        }
        return super.update(id, teacher);
    }

    private void normalize(Teacher teacher) {
        teacher.setTeacherCode(teacher.getTeacherCode().trim().toUpperCase());
        teacher.setEmail(teacher.getEmail().trim().toLowerCase());
    }
}
