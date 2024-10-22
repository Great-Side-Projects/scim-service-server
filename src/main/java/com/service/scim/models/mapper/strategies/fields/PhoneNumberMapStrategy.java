package com.service.scim.models.mapper.strategies.fields;

import com.service.scim.models.User;

import java.util.ArrayList;
import java.util.Map;

public class PhoneNumberMapStrategy implements IMapStrategy<User> {

    @Override
    public void applyUpdate(User entity, String field, Object value) {

        ((ArrayList) value).forEach(phoneNumber -> {
            Map<String, Object> phone = (Map<String, Object>) phoneNumber;
            if (((Boolean) phone.get("primary")))
                entity.primaryPhone = phone.get("value").toString();
            if (phone.get("type").equals("mobile"))
                entity.mobilePhone = phone.get("value").toString();
            if (phone.get("type").equals("work"))
                entity.businessPhone = phone.get("value").toString();
        });
    }
}
