package com.service.scim.models.mapper;

import com.service.scim.models.User;
import java.util.ArrayList;
import java.util.Map;

public class EmailIMapStrategy implements IMapStrategy {

        @Override
        public void applyUpdate(User user, String field, Object value) {

            ((ArrayList)value).forEach(email -> {
                Map<String, Object> values = (Map<String, Object>) email;
                if (((Boolean) values.get("primary")))
                    user.email = values.get("value").toString();
                if (values.get("type").equals("other"))
                    user.secondEmail = values.get("value").toString();
            });
        }
}
