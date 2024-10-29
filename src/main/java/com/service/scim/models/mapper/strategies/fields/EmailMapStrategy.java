package com.service.scim.models.mapper.strategies.fields;

import com.service.scim.models.User;
import java.util.ArrayList;
import java.util.Map;

public class EmailMapStrategy implements IMapStrategy<User> {

        @Override
        public void applyUpdate(User entity, String field, Object value) {

            ((ArrayList)value).forEach(email -> {
                Map<String, Object> values = (Map<String, Object>) email;
                if (((Boolean) values.get("primary")))
                    entity.email = values.get("value").toString();
                if (values.get("type").equals("other"))
                    entity.secondEmail = values.get("value").toString();
            });
        }
}
