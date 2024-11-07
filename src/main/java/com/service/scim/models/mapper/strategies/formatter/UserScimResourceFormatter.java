package com.service.scim.models.mapper.strategies.formatter;

import com.service.scim.models.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserScimResourceFormatter implements IScimResourceFormatter<User> {

    @Override
    public Map<String, Object> toScimResource(User user) {
        Map<String, Object> returnValue = new HashMap<>();
        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:schemas:core:2.0:User");
        returnValue.put("schemas", schemas);
        returnValue.put("id", user.id);
        returnValue.put("active", user.active);
        returnValue.put("userName", user.userName);

        // Name
        Map<String, Object> names = new HashMap<>();
        names.put("familyName", user.familyName);
        names.put("givenName", user.givenName);
        names.put("middleName", user.middleName);
        names.put("displayName", user.displayName);
        returnValue.put("name", names);

        // Meta information
        Map<String, Object> meta = new HashMap<>();
        meta.put("resourceType", "User");
        meta.put("location", ("/scim/v2/Users/" + user.id));
        returnValue.put("meta", meta);

        List<Map<String, Object>> emails = new ArrayList<>();
        Map<String, Object> primaryEmail = new HashMap<>();
        primaryEmail.put("primary", true);
        primaryEmail.put("value", user.email);
        primaryEmail.put("type", "work");
        emails.add(primaryEmail);
        returnValue.put("emails", emails);

        return returnValue;
    }
}
