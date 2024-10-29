package com.service.scim.models.mapper.strategies.fields;

import com.service.scim.models.User;
import java.util.ArrayList;
import java.util.Map;

public class AddressMapStrategy implements IMapStrategy<User> {

    @Override
    public void applyUpdate(User entity, String field, Object value) {

        ((ArrayList) value).forEach(address -> {
            Map<String, Object> mapAddress = (Map<String, Object>) address;
            if (mapAddress.get("type").equals("work") && mapAddress.containsKey("formatted")) {
                entity.postalAddress = mapAddress.get("formatted").toString();
            }
        });
    }
}
