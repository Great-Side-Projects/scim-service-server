package com.service.scim.com.service.scim.models.mapper.strategies;

import static org.mockito.Mockito.*;
import com.service.scim.models.User;
import com.service.scim.models.mapper.UserEntityMapper;
import com.service.scim.models.mapper.strategies.fields.IMapStrategy;
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
    private IMapStrategy<User> genericMapStrategy;

    @Mock
    private IMapStrategy<User> addressMapStrategy;

    @Mock
    private IMapStrategy<User> emailMapStrategy;

    @Mock
    private IMapStrategy<User> phoneNumberMapStrategy;

    @Mock
    private IMapStrategy<User> enterpriseMapStrategy;

    @InjectMocks
    private UserEntityMapper userEntityMapper;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntityMapper = new UserEntityMapper(genericMapStrategy);
        userEntityMapper.updateStrategy.strategies.put("addresses", addressMapStrategy);
        userEntityMapper.updateStrategy.strategies.put("emails", emailMapStrategy);
        userEntityMapper.updateStrategy.strategies.put("phoneNumbers", phoneNumberMapStrategy);
        userEntityMapper.updateStrategy.strategies.put("manager", enterpriseMapStrategy);
        user = new User();
    }

    @Test
    void updateAppliesAddressStrategyToEntity() {
        ArrayList<Map<String, Object>> addresses = new ArrayList<>();
        addresses.add(Map.of("streetAddress", "123 Main St", "type", "work", "primary", true));
        Map<String, Object> resource = new HashMap<>();
        resource.put("addresses", addresses);

        userEntityMapper.update(user, resource);

        verify(addressMapStrategy, times(1)).applyUpdate(eq(user), eq("addresses"), eq(addresses));
    }

    @Test
    void updateAppliesEmailStrategyToEntity() {
        ArrayList<Map<String, Object>> emails = new ArrayList<>();
        emails.add(Map.of("primary", true, "type", "work", "value", "john.doe@example.com"));
        Map<String, Object> resource = new HashMap<>();
        resource.put("emails", emails);

        userEntityMapper.update(user, resource);

        verify(emailMapStrategy, times(1)).applyUpdate(eq(user), eq("emails"), eq(emails));
    }

    @Test
    void updateAppliesPhoneNumberStrategyToEntity() {
        ArrayList<Map<String, Object>> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(Map.of("value", "123-456-7890", "type", "mobile", "primary", true));
        Map<String, Object> resource = new HashMap<>();
        resource.put("phoneNumbers", phoneNumbers);

        userEntityMapper.update(user, resource);

        verify(phoneNumberMapStrategy, times(1)).applyUpdate(eq(user), eq("phoneNumbers"), eq(phoneNumbers));
    }

    @Test
    void updateAppliesManagerStrategyToEntity() {
        Map<String, Object> resource = new HashMap<>();
        resource.put("manager", "Manager Name");

        userEntityMapper.update(user, resource);

        verify(enterpriseMapStrategy, times(1)).applyUpdate(eq(user), eq("manager"), eq("Manager Name"));
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