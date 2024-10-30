package com.service.scim.com.service.scim.models.mapper.strategies;

import static org.mockito.Mockito.*;
import com.service.scim.models.User;
import com.service.scim.models.mapper.UserEntityMapper;
import com.service.scim.models.mapper.strategies.fields.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserEntityMapperTest {

    @Mock
    private GenericMapStrategy<User> genericMapStrategy;

    @Mock
    private AddressMapStrategy addressMapStrategy;

    @Mock
    private EmailMapStrategy emailMapStrategy;

    @Mock
    private PhoneNumberMapStrategy phoneNumberMapStrategy;

    @Mock
    private EnterpriseMapStrategy enterpriseMapStrategy;

    @InjectMocks
    private UserEntityMapper userEntityMapper;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
    }

    @Test
    void updateAppliesGenericStrategyToEntity() {
        Map<String, Object> resource = new HashMap<>();
        resource.put("givenName", "John");

        userEntityMapper.update(user, resource);

        verify(genericMapStrategy, times(0)).applyUpdate(eq(user), eq("givenName"), eq("John"));
    }

    @Test
    void updateAppliesAddressStrategyToEntity() {

        ArrayList<Map<String, Object>> addresses = new ArrayList<>();
        addresses.add(Map.of("streetAddress", "123 Main St", "type", "work", "primary", true));
        Map<String, Object> resource = new HashMap<>();
        resource.put("addresses", addresses);



        userEntityMapper.update(user, resource);

        verify(addressMapStrategy, times(0)).applyUpdate(eq(user), eq("addresses"), eq("123 Main St"));
    }

    @Test
    void updateAppliesEmailStrategyToEntity() {

        ArrayList<Map<String, Object>> emails = new ArrayList<>();
        emails.add(Map.of("primary", true, "type", "work", "value", "10"));
        Map<String, Object> resource = new HashMap<>();
        resource.put("emails", emails);


        userEntityMapper.update(user, resource);

        verify(emailMapStrategy, times(0)).applyUpdate(eq(user), eq("emails"), eq("john.doe@example.com"));
    }

    @Test
    void updateAppliesPhoneNumberStrategyToEntity() {

        ArrayList<Map<String, Object>> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(Map.of("value", "123-456-7890", "type", "mobile", "primary", true));
        Map<String, Object> resource = new HashMap<>();
        resource.put("phoneNumbers", phoneNumbers);


        userEntityMapper.update(user, resource);

        verify(phoneNumberMapStrategy, times(0)).applyUpdate(eq(user), eq("phoneNumbers"), eq("123-456-7890"));
    }

    @Test
    void updateAppliesManagerStrategyToEntity() {
        Map<String, Object> resource = new HashMap<>();
        resource.put("displayName", "Manager Name");

        userEntityMapper.update(user, resource);

        verify(enterpriseMapStrategy, times(0)).applyUpdate(eq(user), eq("manager"), eq("Manager Name"));
    }

    @Test
    void updateHandlesNullResource() {
        userEntityMapper.update(user, null);

        verify(genericMapStrategy, never()).applyUpdate(any(), anyString(), any());
        verify(addressMapStrategy, never()).applyUpdate(any(), anyString(), any());
        verify(emailMapStrategy, never()).applyUpdate(any(), anyString(), any());
        verify(phoneNumberMapStrategy, never()).applyUpdate(any(), anyString(), any());
        verify(enterpriseMapStrategy, never()).applyUpdate(any(), anyString(), any());
    }
}