package com.altamirano_pajares.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean // no crea un bean para esta interfaz, solo para sus hijas
public interface IGenericRepository<T, ID> extends JpaRepository<T, ID> {
}
