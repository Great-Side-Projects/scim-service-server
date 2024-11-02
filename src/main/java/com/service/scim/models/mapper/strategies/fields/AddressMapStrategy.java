package com.service.scim.models.mapper.strategies.fields;

import com.service.scim.models.User;
import java.util.ArrayList;
import java.util.Map;

public class AddressMapStrategy implements IMapStrategy<User> {

    @Override
    public void applyUpdate(User entity, String field, Object value) {

        if (value == null)
            return;

        ((ArrayList) value).forEach(address -> {
            Map<String, Object> mapAddress = (Map<String, Object>) address;
            if (mapAddress.get("type").equals("work")) {
                entity.postalAddress = (String) mapAddress.getOrDefault("formatted", null);
                entity.country = (String) mapAddress.getOrDefault("country", null);
                entity.locality = (String) mapAddress.getOrDefault("locality", null);
                entity.region = (String) mapAddress.getOrDefault("region", null);
                entity.streetAddress = (String) mapAddress.getOrDefault("streetAddress", null);
                entity.postalCode = (String) mapAddress.getOrDefault("postalCode", null);
            }
        });
    }
}
