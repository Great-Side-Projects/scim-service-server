package com.service.scim.models.mapper;

import com.service.scim.models.User;
import java.util.ArrayList;
import java.util.Map;

public class AddressIMapStrategy implements IMapStrategy {

    @Override
    public void applyUpdate(User user, String field, Object value) {

        ((ArrayList) value).forEach(address -> {
            Map<String, Object> mapAddress = (Map<String, Object>) address;
            if (mapAddress.get("type").equals("work") && mapAddress.containsKey("formatted")) {
                user.postalAddress = mapAddress.get("formatted").toString();
            }
        });
    }
}
