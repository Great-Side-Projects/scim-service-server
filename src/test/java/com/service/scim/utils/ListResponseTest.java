package com.service.scim.utils;

import com.service.scim.models.User;
import com.service.scim.models.mapper.strategies.groupmembership.IGroupMembershipAssigner;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListResponseTest {

    @Test
    void toScimResourceReturnsCorrectJsonStructure() {
        User user1 = new User();
        user1.id = "1";
        user1.userName = "user1";
        User user2 = new User();
        user2.id = "2";
        user2.userName = "user2";
        List<User> users = List.of(user1, user2);
        ListResponse<User> listResponse = new ListResponse.Builder<User>()
                .withList(users)
                .withStartIndex(1)
                .withCount(2)
                .withTotalResults(2)
                .build();

        Map<String, Object> result = listResponse.toScimResource();

        assertNotNull(result);
        assertEquals(2, result.get("totalResults"));
        assertEquals(1, result.get("startIndex"));
        assertEquals(2, result.get("itemsPerPage"));
        assertTrue(((List<?>) result.get("Resources")).size() == 2);
        assertTrue(((List<?>) result.get("schemas")).contains("urn:ietf:params:scim:api:messages:2.0:ListResponse"));
    }

    @Test
    void toScimResourceHandlesEmptyList() {
        ListResponse<User> listResponse = new ListResponse.Builder<User>()
                .withList(new ArrayList<>())
                .withStartIndex(1)
                .withCount(0)
                .withTotalResults(0)
                .build();

        Map<String, Object> result = listResponse.toScimResource();

        assertNotNull(result);
        assertEquals(0, result.get("totalResults"));
        assertEquals(1, result.get("startIndex"));
        assertEquals(0, result.get("itemsPerPage"));
        assertTrue(((List<?>) result.get("Resources")).isEmpty());
        assertTrue(((List<?>) result.get("schemas")).contains("urn:ietf:params:scim:api:messages:2.0:ListResponse"));
    }

    @Test
    void toScimResourceAppliesMembershipAssigner() {
        IGroupMembershipAssigner<User> membershipAssigner = mock(IGroupMembershipAssigner.class);
        User user1 = new User();
        user1.id = "1";
        user1.userName = "user1";
        User user2 = new User();
        user2.id = "2";
        user2.userName = "user2";
        List<User> users = List.of(user1, user2);
        ListResponse<User> listResponse = new ListResponse.Builder<User>()
                .withList(users)
                .withStartIndex(1)
                .withCount(2)
                .withTotalResults(2)
                .withMembershipAssigner(membershipAssigner)
                .build();

        listResponse.toScimResource();

        verify(membershipAssigner, times(1)).assignMemberships(anyList(), eq(users));
    }

    @Test
    void toScimResourceHandlesNullMembershipAssigner() {
        User user1 = new User();
        user1.id = "1";
        user1.userName = "user1";
        User user2 = new User();
        user2.id = "2";
        user2.userName = "user2";
        List<User> users = List.of(user1, user2);
        ListResponse<User> listResponse = new ListResponse.Builder<User>()
                .withList(users)
                .withStartIndex(1)
                .withCount(2)
                .withTotalResults(2)
                .withMembershipAssigner(null)
                .build();

        Map<String, Object> result = listResponse.toScimResource();

        assertNotNull(result);
        assertEquals(2, result.get("totalResults"));
        assertEquals(1, result.get("startIndex"));
        assertEquals(2, result.get("itemsPerPage"));
        assertTrue(((List<?>) result.get("Resources")).size() == 2);
        assertTrue(((List<?>) result.get("schemas")).contains("urn:ietf:params:scim:api:messages:2.0:ListResponse"));
    }
}