package com.service.scim.services;

import com.service.scim.models.GroupMembership;
import com.service.scim.models.User;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.repositories.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

public class UsersServiceTest {
    @Mock
    private IUserRepository userRepository;

    @Mock
    private IGroupMembershipRepository groupMembershipRepository;

    @InjectMocks
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void usersGetReturnsUsersWithGroupMemberships() {
        Map<String, String> params = new HashMap<>();
        params.put("excludedAttributes", "none");

        User user = new User();
        user.id = "user1";
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(userPage);

        GroupMembership membership = new GroupMembership();
        membership.userId = "user1";
        Page<GroupMembership> membershipPage = new PageImpl<>(List.of(membership));
        when(groupMembershipRepository.findByUserIds(anyList(), any(PageRequest.class))).thenReturn(membershipPage);

        Map result = usersService.usersGet(params);

        assertNotNull(result);
        assertEquals(1, ((List<?>) result.get("Resources")).size());
    }

    @Test
    void usersGetReturnsUsersWithoutGroupMemberships() {
        Map<String, String> params = new HashMap<>();
        params.put("excludedAttributes", "members");

        User user = new User();
        user.id = "user1";
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(userPage);
        when(groupMembershipRepository.findByUserIds(anyList(), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

        Map result = usersService.usersGet(params);

        assertNotNull(result);
        assertEquals(1, ((List<?>) result.get("Resources")).size());
    }

    @Test
    void usersPostCreatesNewUser() {
        User newUser = new User();
        newUser.displayName = "New User";

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        Map result = usersService.usersPost(newUser);

        assertNotNull(result);
        assertEquals("New User", ((Map)result.get("name")).get("displayName"));
    }
}
