package com.altamirano_pajares.service.implementation;

import com.altamirano_pajares.exception.ModelNotFoundException;
import com.altamirano_pajares.repository.IGenericRepository;
import com.altamirano_pajares.service.IGenericService;

import java.lang.reflect.Method;
import java.util.List;

// Patron Template Method: el CRUD comun vive aqui y cada hija solo entrega su repositorio
public abstract class GenericService<T, ID> implements IGenericService<T, ID> {
    protected abstract IGenericRepository<T, ID> getRepo();

    @Override
    public T save(T t) {
        return getRepo().save(t);
    }

    @Override
    public T update(ID id, T t) {
        findById(id); // 404 si no existe
        setId(t, id);  // asegura un UPDATE y no un INSERT
        return getRepo().save(t);
    }

    @Override
    public List<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    public T findById(ID id) {
        return getRepo().findById(id)
                .orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + id));
    }

    @Override
    public void delete(ID id) {
        findById(id);
        getRepo().deleteById(id);
    }

    // Convencion del proyecto: el id de la entidad X se llama idX (Teacher -> setIdTeacher)
    private void setId(T t, ID id) {
        try {
            Method setter = t.getClass().getMethod("setId" + t.getClass().getSimpleName(), id.getClass());
            setter.invoke(t, id);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("No se pudo asignar el id a " + t.getClass().getSimpleName(), e);
        }
    }
}
