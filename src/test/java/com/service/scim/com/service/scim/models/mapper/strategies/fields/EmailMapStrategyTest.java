package com.service.scim.com.service.scim.models.mapper.strategies.fields;

import static org.junit.jupiter.api.Assertions.*;
import com.service.scim.models.User;
import com.service.scim.models.mapper.strategies.fields.EmailMapStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmailMapStrategyTest {

    private EmailMapStrategy emailMapStrategy;
    private User user;

    @BeforeEach
    void setUp() {
        emailMapStrategy = new EmailMapStrategy();
        user = new User();
    }

    @Test
    void applyUpdateSetsPrimaryEmail() {
        Map<String, Object> email = new HashMap<>();
        email.put("primary", true);
        email.put("value", "primary@example.com");
        email.put("type", "work");
        ArrayList<Map<String, Object>> emails = new ArrayList<>();
        emails.add(email);
        emailMapStrategy.applyUpdate(user, "emails", emails);
        assertEquals("primary@example.com", user.email);
    }

    @Test
    void applyUpdateSetsSecondEmailForOtherType() {
        Map<String, Object> email = new HashMap<>();
        email.put("type", "other");
        email.put("value", "other@example.com");
        email.put("primary", false);
        ArrayList<Map<String, Object>> emails = new ArrayList<>();
        emails.add(email);

        emailMapStrategy.applyUpdate(user, "emails", emails);

        assertEquals("other@example.com", user.secondEmail);
    }

    @Test
    void applyUpdateIgnoresNonPrimaryEmail() {
        Map<String, Object> email = new HashMap<>();
        email.put("primary", false);
        email.put("value", "nonprimary@example.com");
        email.put("type", "work");
        ArrayList<Map<String, Object>> emails = new ArrayList<>();
        emails.add(email);

        emailMapStrategy.applyUpdate(user, "emails", emails);

        assertNull(user.email);
    }

    @Test
    void applyUpdateHandlesEmptyEmailList() {
        ArrayList<Map<String, Object>> emails = new ArrayList<>();

        emailMapStrategy.applyUpdate(user, "emails", emails);

        assertNull(user.email);
        assertNull(user.secondEmail);
    }

    @Test
    void applyUpdateHandlesNullEmailList() {

        emailMapStrategy.applyUpdate(user, "emails", null);

        assertNull(user.email);
        assertNull(user.secondEmail);
    }
}
