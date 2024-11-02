package com.service.scim.com.service.scim.models.mapper.strategies;

import static org.mockito.Mockito.*;
import com.service.scim.models.Group;
import com.service.scim.models.mapper.GroupEntityMapper;
import com.service.scim.models.mapper.strategies.fields.GenericMapStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashMap;
import java.util.Map;

public class GroupEntityMapperTest {

    @Mock
    private GenericMapStrategy<Group> genericMapStrategy;

    @InjectMocks
    private GroupEntityMapper groupEntityMapper;

    private Group group;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        groupEntityMapper = new GroupEntityMapper(genericMapStrategy);
        group = new Group();
    }

    @Test
    void updateAppliesStrategiesToEntity() {
        Map<String, Object> resource = new HashMap<>();
        resource.put("displayName", "New Group Name");

        groupEntityMapper.update(group, resource);

        verify(genericMapStrategy, times(1)).applyUpdate(eq(group), eq("displayName"), eq("New Group Name"));
    }


    @Test
    void updateHandlesNullResource() {
        groupEntityMapper.update(group, null);

        verify(genericMapStrategy, never()).applyUpdate(any(), anyString(), any());
    }
}
