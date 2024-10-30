package com.service.scim.utils;

import com.service.scim.models.User;
import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class ListResponseTest {

    @Test
    void toScimResourceReturnsCorrectJsonStructure() {
        User user = new User();
        user.id = "123";
        user.userName = "user1";
        User user2 = new User();
        user2.id = "456";
        user2.userName = "user2";

        List<User> users = List.of(user, user2);
        GroupMembership membership = new GroupMembership();
        membership.id = "menber1";
        membership.groupId = "group1";
        membership.userId = "123";
        GroupMembership membership2 = new GroupMembership();
        membership2.id = "menber2";
        membership2.groupId = "group2";
        membership2.userId = "456";

        List<GroupMembership> memberships = List.of(membership, membership2);
        ListResponse<User> response = new ListResponse<>(users, Optional.of(1), Optional.of(2), Optional.of(2), memberships);

        HashMap<String, Object> result = response.toScimResource();

        assertNotNull(result);
        assertEquals(2, result.get("totalResults"));
        assertEquals(1, result.get("startIndex"));
        assertEquals(2, ((List<?>) result.get("Resources")).size());
    }

    @Test
    void toScimResourceHandlesEmptyList() {
        ListResponse<User> response = new ListResponse<>(new ArrayList<>(), Optional.of(1), Optional.of(0), Optional.of(0), new ArrayList<>());

        HashMap<String, Object> result = response.toScimResource();

        assertNotNull(result);
        assertEquals(0, result.get("totalResults"));
        assertEquals(1, result.get("startIndex"));
        assertTrue(((List<?>) result.get("Resources")).isEmpty());
    }

    @Test
    void toScimResourceHandlesNullGroupMemberships() {
        User user = new User();
        user.id = "123";
        user.userName = "user1";

        List<User> users = List.of(user);
        ListResponse<User> response = new ListResponse<>(users, Optional.of(1), Optional.of(1), Optional.of(1), null);

        HashMap<String, Object> result = response.toScimResource();

        assertNotNull(result);
        assertEquals(1, result.get("totalResults"));
        assertEquals(1, result.get("startIndex"));
        assertEquals(1, ((List<?>) result.get("Resources")).size());
    }

    @Test
    void toScimResourceHandlesGroupMembershipsForUsers() {
        User user = new User();
        user.id = "123";
        user.userName = "user1";
        GroupMembership membership = new GroupMembership();
        membership.id = "menber1";
        membership.groupId = "group1";
        membership.userId = "123";
        List<User> users = List.of(user);
        List<GroupMembership> memberships = List.of(membership);
        ListResponse<User> response = new ListResponse<>(users, Optional.of(1), Optional.of(1), Optional.of(1), memberships);

        HashMap<String, Object> result = response.toScimResource();

        assertNotNull(result);
        assertEquals(1, result.get("totalResults"));
        assertEquals(1, result.get("startIndex"));
        List<?> resources = (List<?>) result.get("Resources");
        assertEquals(1, resources.size());
        Map<?, ?> userResource = (Map<?, ?>) resources.get(0);
        assertTrue(userResource.containsKey("groups"));
        assertEquals(1, ((List<?>) userResource.get("groups")).size());
    }

    @Test
    void toScimResourceHandlesGroupMembershipsForGroups() {
        Group group = new Group();
        group.id = "123";
        group.displayName = "group1";
        List<Group> groups = List.of(group);
        GroupMembership membership = new GroupMembership();
        membership.id = "member1";
        membership.groupId = "123";
        membership.userId = "user1";

        List<GroupMembership> memberships = List.of(membership);
        ListResponse<Group> response = new ListResponse<>(
                groups,
                Optional.of(1),
                Optional.of(1),
                Optional.of(1),
                memberships);

        HashMap<String, Object> result = response.toScimResource();

        assertNotNull(result);
        assertEquals(1, result.get("totalResults"));
        assertEquals(1, result.get("startIndex"));
        List<?> resources = (List<?>) result.get("Resources");
        assertEquals(1, resources.size());
        Map<?, ?> groupResource = (Map<?, ?>) resources.get(0);
        assertTrue(groupResource.containsKey("members"));
        assertEquals(1, ((List<?>) groupResource.get("members")).size());
    }
}
