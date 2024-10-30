package com.service.scim.models.mapper.strategies.fields;

import com.service.scim.models.User;

import java.util.ArrayList;
import java.util.Map;

public class EmailMapStrategy implements IMapStrategy<User> {

    @Override
    public void applyUpdate(User entity, String field, Object value) {

        if (value == null)
            return;

        ((ArrayList) value).forEach(email -> {
            Map<String, Object> values = (Map<String, Object>) email;
            if (((Boolean) values.get("primary")))
                entity.email = (String) values.getOrDefault("value", null);
            if (values.get("type").equals("other"))
                entity.secondEmail = (String) values.getOrDefault("value", null);
        });
    }
}
