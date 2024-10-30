package com.service.scim.services;

import com.service.scim.models.User;
import com.service.scim.models.mapper.UserEntityMapper;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.repositories.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SingleUserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private SingleUserService singleUserService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.singleUserService = new SingleUserService(userRepository, new UserEntityMapper());
    }

    @Test
    void singleUserGetReturnsUser() {
        String userId = "user1";
        User user = new User();
        user.id = userId;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Map result = singleUserService.singeUserGet(userId);

        assertNotNull(result);
        assertEquals(userId, result.get("id"));
    }

    @Test
    void singleUserGetReturnsEmptyMapForNonExistentUser() {
        String userId = "nonExistentUser";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Map result = singleUserService.singeUserGet(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void singleUserPutUpdatesUser() {
        String userId = "user1";
        Map<String, Object> payload = new HashMap<>();
        payload.put("displayName", "Updated User");

        User user = new User();
        user.id = userId;
        user.displayName = "Old User";

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Map result = singleUserService.singleUserPut(payload, userId);

        assertNotNull(result);
        assertEquals("Updated User", ((Map)result.get("name")).get("displayName"));
    }

    @Test
    void singleUserPutReturnsEmptyMapForNonExistentUser() {
        String userId = "nonExistentUser";
        Map<String, Object> payload = new HashMap<>();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Map result = singleUserService.singleUserPut(payload, userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void singleUserPatchUpdatesUser() {
        String userId = "user1";
        Map<String, Object> payload = new HashMap<>();
        payload.put("Operations", new ArrayList<Map<String, Object>>() {{
            add(new HashMap<String, Object>() {{
                put("op", "Replace");
                put("path", "displayName");
                put("value", "Patched User");
            }});
        }});


        User user = new User();
        user.id = userId;
        user.displayName = "Old User";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Map result = singleUserService.singleUserPatch(payload, userId);

        assertNotNull(result);
        assertEquals("Patched User", ((Map)result.get("name")).get("displayName"));
    }

    @Test
    void singleUserPatchReturnsEmptyMapForNonExistentUser() {
        String userId = "nonExistentUser";
        Map<String, Object> payload = new HashMap<>();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Map result = singleUserService.singleUserPatch(payload, userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void singleUserDeleteRemovesUser() {
        String userId = "user1";
        User user = new User();
        user.id = userId;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        singleUserService.singleUserDelete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
