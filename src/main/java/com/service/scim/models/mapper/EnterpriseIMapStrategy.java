package com.service.scim.models.mapper;

import com.service.scim.models.User;
import java.util.Map;

public class EnterpriseIMapStrategy implements IMapStrategy {

        @Override
        public void applyUpdate(User user, String field, Object value) {
            // Enterprise attributes
                Map<String, Object> manager = (Map<String, Object>)value;
                user.manager = manager.get("displayName").toString();
        }
}
