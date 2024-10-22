package com.service.scim.models.mapper.strategies.fields;

public interface IMapStrategy<T> {
    void applyUpdate(T entity, String field, Object value);
}
