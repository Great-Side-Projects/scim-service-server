package com.service.scim.models.mapper;

import com.service.scim.models.User;
import com.service.scim.models.mapper.strategies.fields.*;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserEntityMapper extends AbstractEntityMapper<User> {

    public UserEntityMapper() {
        super(new GenericMapStrategy<>(User.class));
    }

    @Override
    protected Map<String, IMapStrategy<User>> initializeStrategies() {
        Map<String, IMapStrategy<User>> strategy = new HashMap<>();
        strategy.put("addresses", new AddressMapStrategy());
        strategy.put("emails", new EmailMapStrategy());
        strategy.put("phoneNumbers", new PhoneNumberMapStrategy());
        strategy.put("manager", new EnterpriseMapStrategy());
        return strategy;
    }
    @Override
    public final void update(User entity, Map<String, Object> resource) {
        if (resource == null)
            return;

        super.update(entity, resource);
    }
}
