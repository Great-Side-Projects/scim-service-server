package com.service.scim.models.mapper;

import com.service.scim.models.mapper.strategies.fields.IMapStrategy;
import com.service.scim.models.mapper.strategies.entities.UpdateStrategy;
import java.util.Map;

public abstract class AbstractEntityMapper<T> {

    public AbstractEntityMapper(IMapStrategy<T> defaultGenericMapStrategy) {
        Map<String, IMapStrategy<T>> mapStrategies = initializeStrategies();
        this.updateStrategy = new UpdateStrategy<>(mapStrategies, defaultGenericMapStrategy);
    }
    // The update strategy for the entity
    public UpdateStrategy<T> updateStrategy;

    // Initialize the strategies for the entity
    protected abstract Map<String, IMapStrategy<T>> initializeStrategies();

    public void update(T entity, Map<String, Object> resource) {
        updateStrategy.update(entity, resource);
    }
}
