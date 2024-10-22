package com.service.scim.models.mapper.strategies.fields;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenericMapStrategy<T> implements IMapStrategy<T> {

    private static final Map<String, String> mappedProperty = new HashMap<>();
    private static final ArrayList<String> declaredFields = new ArrayList<>();
    private final Class<T> entity;

    public GenericMapStrategy(Class<T> entity) {
        this.entity = entity;
        InitMappedProperty();
        InitDeclaredFields();
    }

    private void InitDeclaredFields() {
        Field[] fields = entity.getDeclaredFields();
        for (Field field : fields) {
            declaredFields.add(field.getName());
        }
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
    public void applyUpdate(T entity, String field, Object value) {

        field = this.IsMappedProperty(field) ? this.getMappedProperty(field) : field;
        // Use Java reflection to find and set User attribute
        try {
            // validate key
            if (!declaredFields.contains(field)) {
                return;
            }
            Field classfield = entity.getClass().getDeclaredField(field);
            classfield.set(entity, value);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            System.out.println(e);
            // Error - Do not update field
        }
    }
}
