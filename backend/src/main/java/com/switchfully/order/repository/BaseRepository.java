package com.switchfully.order.repository;

import com.switchfully.order.domain.BaseEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

public abstract class BaseRepository<T extends BaseEntity> {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public T save(T entity) {
        entity.generateId();
        entityManager.persist(entity);
        return entity;
    }

    public T update(T entity) {
        entityManager.merge(entity);
        return entity;
    }

    public abstract List<T> getAll();
    public abstract T get(UUID entityId);

}
