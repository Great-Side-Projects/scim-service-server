package com.service.scim.models.mapper;

import java.util.ArrayList;
import java.util.Map;

public class UpdateStrategy<T> {

    protected Map<String, IMapStrategy<T>> strategies;
    protected IMapStrategy<T> defaultGenericMapStrategy;

    public UpdateStrategy(Map<String, IMapStrategy<T>> strategies, IMapStrategy<T> defaultGenericMapStrategy) {
        this.strategies = strategies;
        this.defaultGenericMapStrategy = defaultGenericMapStrategy;
    }

    void update(T entity, Map<String, Object> resource) {
        resource.forEach((field, value) -> {

            IsMapRecursively(entity, value);
            // Check if the value is a list or a map and call the update method recursively
            if (value instanceof ArrayList<?> MyArrayList) {
                MyArrayList.forEach(subResource -> {
                    IsMapRecursively(entity, subResource);
                });
            }
            IMapStrategy<T> strategy = strategies.getOrDefault(field, defaultGenericMapStrategy);
            strategy.applyUpdate(entity, field, value);
        });
    }

    private void IsMapRecursively(T entity, Object subResource) {
        // Check if the value is a map and call the update method recursively
        if (subResource instanceof Map<?, ?> MyMap) {
            update(entity, (Map<String, Object>) MyMap);
        }
    }
}
