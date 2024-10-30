package com.service.scim.com.service.scim.models.mapper.strategies.fields;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.service.scim.models.User;
import com.service.scim.models.mapper.strategies.fields.AddressMapStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class AddressMapStrategyTest {

    @InjectMocks
    private AddressMapStrategy addressMapStrategy;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
    }

    @Test
    void applyUpdateSetsPostalAddressForWorkType() {
        Map<String, Object> address = new HashMap<>();
        address.put("type", "work");
        address.put("formatted", "123 Work St");
        ArrayList<Map<String, Object>> addresses = new ArrayList<>();
        addresses.add(address);

        addressMapStrategy.applyUpdate(user, "addresses", addresses);

        assertEquals("123 Work St", user.postalAddress);
    }

    @Test
    void applyUpdateIgnoresNonWorkType() {
        Map<String, Object> address = new HashMap<>();
        address.put("type", "home");
        address.put("formatted", "123 Home St");
        ArrayList<Map<String, Object>> addresses = new ArrayList<>();
        addresses.add(address);

        addressMapStrategy.applyUpdate(user, "addresses", addresses);

        assertNull(user.postalAddress);
    }

    @Test
    void applyUpdateIgnoresAddressWithoutFormatted() {
        Map<String, Object> address = new HashMap<>();
        address.put("type", "work");
        ArrayList<Map<String, Object>> addresses = new ArrayList<>();
        addresses.add(address);

        addressMapStrategy.applyUpdate(user, "addresses", addresses);

        assertNull(user.postalAddress);
    }

    @Test
    void applyUpdateHandlesEmptyAddressList() {
        ArrayList<Map<String, Object>> addresses = new ArrayList<>();

        addressMapStrategy.applyUpdate(user, "addresses", addresses);

        assertNull(user.postalAddress);
    }

    @Test
    void applyUpdateHandlesNullAddressList() {

        addressMapStrategy.applyUpdate(user, "addresses", null);

        assertNull(user.postalAddress);
    }
}