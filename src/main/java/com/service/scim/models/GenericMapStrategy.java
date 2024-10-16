package com.service.scim.models;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GenericMapStrategy implements MapStrategy {

    private static final Map<String, String> mappedProperty = new HashMap<>();

    public GenericMapStrategy() {
        InitMappedProperty();
    }

    private boolean IsMappedProperty(String key) {
        return mappedProperty.containsKey(key);
    }

    private String getMappedProperty(String Key) {
        return mappedProperty.get(Key);
    }

    private static void InitMappedProperty() {
        mappedProperty.put("addresses[type eq \"work\"].formatted", "postalAddress");
        mappedProperty.put("addresses[type eq \"work\"].streetAddress", "streetAddress");
        mappedProperty.put("addresses[type eq \"work\"].locality", "locality");
        mappedProperty.put("addresses[type eq \"work\"].postalCode", "postalCode");
        mappedProperty.put("addresses[type eq \"work\"].country", "country");
        mappedProperty.put("phoneNumbers[type eq \"work\"].value", "businessPhone");
        mappedProperty.put("phoneNumbers[type eq \"mobile\"].value", "mobilePhone");
        mappedProperty.put("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:employeeNumber", "employeeNumber");
        mappedProperty.put("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:department", "department");
        mappedProperty.put("addresses[type eq \"work\"].region", "region");
        mappedProperty.put("emails[type eq \"work\"].value", "email");
        mappedProperty.put("name.givenName", "givenName");
        mappedProperty.put("name.familyName", "familyName");
        mappedProperty.put("name.middleName", "middleName");
        mappedProperty.put("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:manager", "manager");
        mappedProperty.put("emails[type eq \"other\"].value", "secondEmail");
    }

    @Override
    public void applyUpdate(User user, String key, Object value) {

        key = this.IsMappedProperty(key) ? this.getMappedProperty(key) : key;
        // Use Java reflection to find and set User attribute
        try {
            Field field = user.getClass().getDeclaredField(key);
            field.set(user, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Error - Do not update field
        }
    }
}
