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

    @Override
    protected Map<String, IMapStrategy<Group>> initializeStrategies() {
        // add the strategies for the Group fields here
        //strategy.put("members", new MemberMapStrategy());
        return new HashMap<>();
    }
    @Override
    public final void update(Group entity, Map<String, Object> resource) {
        super.update(entity, resource);
    }
}
