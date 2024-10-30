package com.service.scim.com.service.scim.models.mapper.strategies.fields;

import static org.junit.jupiter.api.Assertions.*;
import com.service.scim.models.User;
import com.service.scim.models.mapper.strategies.fields.GenericMapStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GenericMapStrategyTest {

    private GenericMapStrategy<User> genericMapStrategy;
    private User user;

    @BeforeEach
    void setUp() {
        genericMapStrategy = new GenericMapStrategy<>(User.class);
        user = new User();
    }

    @Test
    void applyUpdateSetsMappedProperty() {
        genericMapStrategy.applyUpdate(user, "addresses[type eq \"work\"].formatted", "123 Work St");

        assertEquals("123 Work St", user.postalAddress);
    }

    @Test
    void applyUpdateSetsDirectProperty() {
        genericMapStrategy.applyUpdate(user, "givenName", "John");

        assertEquals("John", user.givenName);
    }

    @Test
    void applyUpdateIgnoresUnknownProperty() {
        genericMapStrategy.applyUpdate(user, "unknownField", "value");

        assertNull(user.id);
    }

    @Test
    void applyUpdateHandlesNullValue() {
        genericMapStrategy.applyUpdate(user, "givenName", null);

        assertNull(user.givenName);
    }

    @Test
    void applyUpdateHandlesInvalidField() {
        assertDoesNotThrow(() -> genericMapStrategy.applyUpdate(user, "invalidField", "value"));
    }
}
