package com.service.scim.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import com.service.scim.models.mapper.AbstractEntityMapper;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.repositories.IGroupRepository;
import com.service.scim.repositories.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import java.util.*;

public class GroupsServiceTest {

    @Mock
    private IGroupRepository groupRepository;

    @Mock
    private IGroupMembershipRepository groupMembershipRepository;

    @Mock
    private AbstractEntityMapper<Group> groupEntityMapper;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private GroupsService groupsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void groupsGetReturnsGroupsWithMembers() {
        Map<String, String> params = new HashMap<>();
        params.put("excludedAttributes", "none");

        Group group = new Group();
        group.id = "group1";
        Page<Group> groupPage = new PageImpl<>(List.of(group));
        when(groupRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(groupPage);

        GroupMembership membership = new GroupMembership();
        membership.groupId = "group1";
        Page<GroupMembership> membershipPage = new PageImpl<>(List.of(membership));
        when(groupMembershipRepository.findByGroupIds(anyList(), any(PageRequest.class))).thenReturn(membershipPage);

        Map result = groupsService.groupsGet(params);

        assertNotNull(result);
        assertEquals(1, ((List<?>) result.get("Resources")).size());
    }

    @Test
    void groupsGetReturnsGroupsWithoutMembers() {
        Map<String, String> params = new HashMap<>();
        params.put("excludedAttributes", "members");

        Group group = new Group();
        group.id = "group1";
        Page<Group> groupPage = new PageImpl<>(List.of(group));
        when(groupRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(groupPage);

        Map result = groupsService.groupsGet(params);

        assertNotNull(result);
        assertEquals(1, ((List<?>) result.get("Resources")).size());
    }

    @Test
    void groupsPostCreatesNewGroup() {
        Map<String, Object> body = new HashMap<>();
        body.put("displayName", "New Group");

        when(groupRepository.existsByDisplayName(anyString())).thenReturn(false);

        Map result = groupsService.groupsPost(body);

        assertNotNull(result);
        assertEquals("New Group", result.get("displayName"));
    }

    @Test
    void groupsPostDoesNotCreateDuplicateGroup() {
        Map<String, Object> body = new HashMap<>();
        body.put("displayName", "Existing Group");

        when(groupRepository.existsByDisplayName(anyString())).thenReturn(true);

        Map result = groupsService.groupsPost(body);

        assertTrue(result.isEmpty());
    }

    @Test
    void groupsPostCreatesGroupWithMembers() {
        ArrayList<Map<String, Object>> members = new ArrayList<>();
        Map<String, Object> member = new HashMap<>();
        member.put("value", "user1");
        members.add(member);
        Map<String, Object> body = new HashMap<>();
        body.put("displayName", "New Group");
        body.put("members", members);

        when(groupRepository.existsByDisplayName(anyString())).thenReturn(false);
        when(userRepository.existsById(anyString())).thenReturn(true);
        when(groupMembershipRepository.existsByGroupIdAndUserId(anyString(), anyString())).thenReturn(false);

        Map result = groupsService.groupsPost(body);

        assertNotNull(result);
        assertEquals("New Group", result.get("displayName"));
        assertEquals(1, ((List<?>) result.get("members")).size());
    }
}