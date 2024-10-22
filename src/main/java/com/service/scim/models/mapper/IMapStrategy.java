package com.service.scim.models.mapper;

public interface IMapStrategy<T> {
    void applyUpdate(T entity, String field, Object value);
}
