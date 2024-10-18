package com.service.scim.models.mapper;

import com.service.scim.models.User;
import java.util.ArrayList;
import java.util.Map;

public class PhoneNumberIMapStrategy implements IMapStrategy {

    @Override
    public void applyUpdate(User user, String field, Object value) {

        ((ArrayList) value).forEach(phoneNumber -> {
            Map<String, Object> phone = (Map<String, Object>) phoneNumber;
            if (((Boolean) phone.get("primary")))
                user.primaryPhone = phone.get("value").toString();
            if (phone.get("type").equals("mobile"))
                user.mobilePhone = phone.get("value").toString();
            if (phone.get("type").equals("work"))
                user.businessPhone = phone.get("value").toString();
        });
    }
}
