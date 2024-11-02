package com.service.scim.com.service.scim.models.mapper.strategies.entities;

import static org.mockito.Mockito.*;
import com.service.scim.models.mapper.strategies.entities.UpdateStrategy;
import com.service.scim.models.mapper.strategies.fields.IMapStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class UpdateStrategyTest {

    @Mock
    private IMapStrategy<Object> defaultGenericMapStrategy;

    @Mock
    private IMapStrategy<Object> specificMapStrategy;

    @InjectMocks
    private UpdateStrategy<Object> updateStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Map<String, IMapStrategy<Object>> strategies = new HashMap<>();
        strategies.put("specificField", specificMapStrategy);
        updateStrategy = new UpdateStrategy<>(strategies, defaultGenericMapStrategy);
    }

    @Test
    void updateAppliesSpecificStrategyForSpecificField() {
        Object entity = new Object();
        Map<String, Object> resource = new HashMap<>();
        resource.put("specificField", "value");

        updateStrategy.update(entity, resource);

        verify(specificMapStrategy, times(1)).applyUpdate(entity, "specificField", "value");
    }

    @Test
    void updateAppliesDefaultStrategyForUnknownField() {
        Object entity = new Object();
        Map<String, Object> resource = new HashMap<>();
        resource.put("unknownField", "value");

        updateStrategy.update(entity, resource);

        verify(defaultGenericMapStrategy, times(1)).applyUpdate(entity, "unknownField", "value");
    }

    @Test
    void updateHandlesNestedMapRecursively() {
        Object entity = new Object();
        Map<String, Object> nestedResource = new HashMap<>();
        nestedResource.put("nestedField", "nestedValue");
        Map<String, Object> resource = new HashMap<>();
        resource.put("nestedMap", nestedResource);

        updateStrategy.update(entity, resource);

        verify(defaultGenericMapStrategy, times(1)).applyUpdate(entity, "nestedField", "nestedValue");
    }

   @Test
    void updateHandlesEmptyResource() {
        Object entity = new Object();
        Map<String, Object> resource = new HashMap<>();

        updateStrategy.update(entity, resource);

        verifyNoInteractions(defaultGenericMapStrategy);
        verifyNoInteractions(specificMapStrategy);
    }
}