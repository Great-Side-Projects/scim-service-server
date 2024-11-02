package com.service.scim.models.mapper.strategies.entities;

import com.service.scim.models.mapper.strategies.fields.IMapStrategy;
import java.util.Map;

public class UpdateStrategy<T> {

    protected Map<String, IMapStrategy<T>> strategies;
    protected IMapStrategy<T> defaultGenericMapStrategy;

    public UpdateStrategy(Map<String, IMapStrategy<T>> strategies, IMapStrategy<T> defaultGenericMapStrategy) {
        this.strategies = strategies;
        this.defaultGenericMapStrategy = defaultGenericMapStrategy;
    }

    public void update(T entity, Map<String, Object> resource) {
        resource.forEach((field, value) -> {

            IsMapRecursively(entity, value);

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
