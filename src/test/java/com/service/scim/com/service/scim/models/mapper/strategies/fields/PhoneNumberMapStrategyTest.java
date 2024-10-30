package com.service.scim.com.service.scim.models.mapper.strategies.fields;

import static org.junit.jupiter.api.Assertions.*;

import com.service.scim.models.User;
import com.service.scim.models.mapper.strategies.fields.PhoneNumberMapStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhoneNumberMapStrategyTest {

    private PhoneNumberMapStrategy phoneNumberMapStrategy;
    private User user;

    @BeforeEach
    void setUp() {
        phoneNumberMapStrategy = new PhoneNumberMapStrategy();
        user = new User();
    }

    @Test
    void applyUpdateSetsPrimaryPhoneNumber() {
        Map<String, Object> phone = new HashMap<>();
        phone.put("primary", true);
        phone.put("value", "123-456-7890");
        phone.put("type", "non");
        ArrayList<Map<String, Object>> phones = new ArrayList<>();
        phones.add(phone);

        phoneNumberMapStrategy.applyUpdate(user, "phoneNumbers", phones);

        assertEquals("123-456-7890", user.primaryPhone);
    }

    @Test
    void applyUpdateSetsMobilePhoneNumber() {
        Map<String, Object> phone = new HashMap<>();
        phone.put("type", "mobile");
        phone.put("value", "098-765-4321");
        phone.put("primary", false);
        ArrayList<Map<String, Object>> phones = new ArrayList<>();
        phones.add(phone);

        phoneNumberMapStrategy.applyUpdate(user, "phoneNumbers", phones);

        assertEquals("098-765-4321", user.mobilePhone);
    }

    @Test
    void applyUpdateSetsBusinessPhoneNumber() {
        Map<String, Object> phone = new HashMap<>();
        phone.put("type", "work");
        phone.put("value", "555-555-5555");
        phone.put("primary", false);
        ArrayList<Map<String, Object>> phones = new ArrayList<>();
        phones.add(phone);

        phoneNumberMapStrategy.applyUpdate(user, "phoneNumbers", phones);

        assertEquals("555-555-5555", user.businessPhone);
    }

    @Test
    void applyUpdateIgnoresNonPrimaryPhoneNumber() {
        Map<String, Object> phone = new HashMap<>();
        phone.put("primary", false);
        phone.put("value", "111-111-1111");
        phone.put("type", "non");
        ArrayList<Map<String, Object>> phones = new ArrayList<>();
        phones.add(phone);

        phoneNumberMapStrategy.applyUpdate(user, "phoneNumbers", phones);

        assertNull(user.primaryPhone);
    }

    @Test
    void applyUpdateHandlesEmptyPhoneList() {
        ArrayList<Map<String, Object>> phones = new ArrayList<>();

        phoneNumberMapStrategy.applyUpdate(user, "phoneNumbers", phones);

        assertNull(user.primaryPhone);
        assertNull(user.mobilePhone);
        assertNull(user.businessPhone);
    }

    @Test
    void applyUpdateHandlesNullPhoneList() {
        phoneNumberMapStrategy.applyUpdate(user, "phoneNumbers", null);

        assertNull(user.primaryPhone);
        assertNull(user.mobilePhone);
        assertNull(user.businessPhone);
    }
}