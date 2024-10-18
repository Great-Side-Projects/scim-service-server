package com.service.scim.models.mapper;

import com.service.scim.models.User;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenericIMapStrategy implements IMapStrategy {

    private static final Map<String, String> mappedProperty = new HashMap<>();
    private static final ArrayList<String> declaredFields = new ArrayList<>();

    public GenericIMapStrategy() {
        InitMappedProperty();
        InitDeclaredFields();
    }

    private void InitDeclaredFields() {
        Field[] fields = User.class.getDeclaredFields();
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
    public void applyUpdate(User user, String field, Object value) {

        field = this.IsMappedProperty(field) ? this.getMappedProperty(field) : field;
        // Use Java reflection to find and set User attribute
        try {
            // validate key
            if (!declaredFields.contains(field)) {
                return;
            }
            Field classfield = user.getClass().getDeclaredField(field);
            classfield.set(user, value);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            System.out.println(e);
            // Error - Do not update field
        }
    }
}
