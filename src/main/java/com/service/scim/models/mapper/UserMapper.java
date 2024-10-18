package com.service.scim.models.mapper;

import com.service.scim.models.User;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserMapper {
    private final Map<String, IMapStrategy> strategies;
    private final GenericIMapStrategy genericMapStrategy;

    public UserMapper() {
        this.genericMapStrategy = new GenericIMapStrategy();
        strategies = InitializeStrategies();
    }

    private Map<String, IMapStrategy> InitializeStrategies() {

        Map<String, IMapStrategy> strategy = new HashMap<>();
        strategy.put("addresses", new AddressIMapStrategy());
        strategy.put("emails", new EmailIMapStrategy());
        strategy.put("phoneNumbers", new PhoneNumberIMapStrategy());
        strategy.put("manager", new EnterpriseIMapStrategy());
        return strategy;
    }

    public void update(User user, Map<String, Object> resource) {

        resource.forEach((field, value) -> {

            IsMapRecursively(user, value);

            IMapStrategy strategy = strategies.getOrDefault(field, genericMapStrategy);
            strategy.applyUpdate(user, field, value);

            // Check if the value is a list or a map and call the update method recursively
            if (value instanceof ArrayList<?> MyArrayList) {
                MyArrayList.forEach(subResource -> {
                    IsMapRecursively(user, subResource);
                });
            }
        });
    }

    private void IsMapRecursively(User user, Object subResource) {
        // Check if the value is a map and call the update method recursively
        if (subResource instanceof Map<?, ?> MyMap) {
            update(user, (Map<String, Object>) MyMap);
        }
    }
}
