package com.service.scim.models;

import java.util.ArrayList;
import java.util.Map;

public class AddressMapStrategy implements MapStrategy {

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
