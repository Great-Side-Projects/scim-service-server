package com.service.scim.models;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserMapper {
    private final Map<String, MapStrategy> strategies;
    private final GenericMapStrategy genericMapStrategy;

    public UserMapper() {
        this.genericMapStrategy = new GenericMapStrategy();
        strategies = InitializeStrategies();
    }

    private Map<String, MapStrategy> InitializeStrategies() {

        Map<String, MapStrategy> strategy = new HashMap<>();
        strategy.put("addresses", new AddressMapStrategy());
        strategy.put("emails", new EmailMapStrategy());
        strategy.put("phoneNumbers", new PhoneNumberMapStrategy());
        strategy.put("manager", new EnterpriseMapStrategy());
        return strategy;
    }

    public void update(User user, Map<String, Object> resource) {

        resource.forEach((field, value) -> {
            MapStrategy strategy = strategies.getOrDefault(field, genericMapStrategy);
            strategy.applyUpdate(user, field, value);

            if (value instanceof ArrayList<?> MyArrayList) {
                MyArrayList.forEach(subResource -> {
                    if (subResource instanceof Map<?, ?> MyMap) {
                        update(user, (Map<String, Object>) MyMap);
                    }
                });
            }

            if (value instanceof Map<?, ?> MyMap) {
                update(user, (Map<String, Object>) MyMap);
            }
        });
    }
}
