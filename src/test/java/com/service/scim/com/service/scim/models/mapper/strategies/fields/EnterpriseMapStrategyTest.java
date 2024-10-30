package com.service.scim.com.service.scim.models.mapper.strategies.fields;

import static org.junit.jupiter.api.Assertions.*;
import com.service.scim.models.User;
import com.service.scim.models.mapper.strategies.fields.EnterpriseMapStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class EnterpriseMapStrategyTest {

    private EnterpriseMapStrategy enterpriseMapStrategy;
    private User user;

    @BeforeEach
    void setUp() {
        enterpriseMapStrategy = new EnterpriseMapStrategy();
        user = new User();
    }

    @Test
    void applyUpdateSetsManagerDisplayName() {
        Map<String, Object> manager = new HashMap<>();
        manager.put("displayName", "John Doe");

        enterpriseMapStrategy.applyUpdate(user, "manager", manager);

        assertEquals("John Doe", user.manager);
    }

    @Test
    void applyUpdateHandlesNullManager() {

        enterpriseMapStrategy.applyUpdate(user, "manager", null);

        assertNull(user.manager);
    }

    @Test
    void applyUpdateHandlesEmptyManagerMap() {
        Map<String, Object> manager = new HashMap<>();

        enterpriseMapStrategy.applyUpdate(user, "manager", manager);

        assertNull(user.manager);
    }

    @Test
    void applyUpdateHandlesManagerWithoutDisplayName() {
        Map<String, Object> manager = new HashMap<>();
        manager.put("id", "12345");

        enterpriseMapStrategy.applyUpdate(user, "manager", manager);

        assertNull(user.manager);
    }
}
