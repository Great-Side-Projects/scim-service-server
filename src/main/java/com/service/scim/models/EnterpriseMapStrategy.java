package com.service.scim.models;

import java.util.Map;

public class EnterpriseMapStrategy implements MapStrategy {

        @Override
        public void applyUpdate(User user, String Field, Object value) {
            // Enterprise attributes
                Map<String, Object> manager = (Map<String, Object>)value;
                user.manager = manager.get("displayName").toString();
        }
}
