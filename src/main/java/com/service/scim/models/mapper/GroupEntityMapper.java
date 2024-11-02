package com.service.scim.models.mapper;

import com.service.scim.models.Group;
import com.service.scim.models.mapper.strategies.fields.GenericMapStrategy;
import com.service.scim.models.mapper.strategies.fields.IMapStrategy;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class GroupEntityMapper extends AbstractEntityMapper<Group> {

    public GroupEntityMapper() {
        super(new GenericMapStrategy<>(Group.class));
    }

    public GroupEntityMapper(IMapStrategy<Group> defaultGenericMapStrategy) {
        super(defaultGenericMapStrategy);
    }

    @Override
    protected Map<String, IMapStrategy<Group>> initializeStrategies() {
        // create the strategies for the entity fields and add them to the map of strategies for the entity
        // Example: strategy.put("addresses", new AddressMapStrategy());
        // Example: strategy.put("members", new MemberMapStrategy());
        return new HashMap<>();
    }
    @Override
    public final void update(Group entity, Map<String, Object> resource) {
        if (resource == null)
            return;

        super.update(entity, resource);
    }
}
