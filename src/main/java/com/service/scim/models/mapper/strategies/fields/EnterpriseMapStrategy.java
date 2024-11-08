package com.service.scim.models.mapper.strategies.fields;

import com.service.scim.models.User;

import java.util.Map;

public class EnterpriseMapStrategy implements IMapStrategy<User> {

    @Override
    public void applyUpdate(User entity, String field, Object value) {

        if (value == null)
            return;

        // Enterprise attributes
        Map<String, Object> manager = (Map<String, Object>) value;
        entity.manager = (String) manager.getOrDefault("displayName", null);
    }
}
