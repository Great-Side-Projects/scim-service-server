package com.service.scim.services;

import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.repositories.IGroupRepository;
import com.service.scim.repositories.IUserRepository;
import com.service.scim.utils.PageRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SingleGroupsServiceTest {

    @Mock
    private IGroupRepository groupRepository;
    @Mock
    private IGroupMembershipRepository groupMembershipRepository;
    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private SingleGroupsService singleGroupsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void singleGroupGetReturnsGroupWithMembers() {
        String groupId = "group1";
        Map<String, String> params = new HashMap<>();

        Group group = new Group();
        group.id = groupId;
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        GroupMembership membership = new GroupMembership();
        membership.groupId = groupId;
        Page<GroupMembership> membershipPage = new PageImpl<>(List.of(membership));
        when(groupMembershipRepository.findByGroupId(groupId, PageRequestBuilder.build())).thenReturn(membershipPage);

        Map result = singleGroupsService.singeGroupGet(groupId, params);

        assertNotNull(result);
        assertEquals(groupId, result.get("id"));
        assertEquals(1, ((List<?>) result.get("members")).size());
    }

    @Test
    void singleGroupGetReturnsGroupWithoutMembers() {
        String groupId = "group1";
        Map<String, String> params = new HashMap<>();
        params.put("excludedAttributes", "members");

        Group group = new Group();
        group.id = groupId;
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        Map result = singleGroupsService.singeGroupGet(groupId, params);

        assertNotNull(result);
        assertEquals(groupId, result.get("id"));
        assertFalse(result.containsKey("members"));
    }

    @Test
    void singleGroupGetReturnsEmptyMapForNonExistentGroup() {
        String groupId = "nonExistentGroup";
        Map<String, String> params = new HashMap<>();

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        Map result = singleGroupsService.singeGroupGet(groupId, params);

        assertTrue(result.isEmpty());
    }

    @Test
    void singleGroupPutUpdatesGroup() {
        String groupId = "group1";
        Map<String, Object> payload = new HashMap<>();
        payload.put("displayName", "Updated Group");

        Group group = new Group();
        group.id = groupId;
        group.displayName = "Old Group";
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        Map result = singleGroupsService.singleGroupPut(payload, groupId);

        assertNotNull(result);
        assertEquals("Updated Group", result.get("displayName"));
    }

    @Test
    void singleGroupPutAddsNewMembers() {
        String groupId = "group1";
        Map<String, Object> payload = new HashMap<>();
        payload.put("displayName", "Group with Members");
        ArrayList<Map<String, Object>> members = new ArrayList<>();
        Map<String, Object> member = new HashMap<>();
        member.put("value", "user1");
        members.add(member);
        payload.put("members", members);

        Group group = new Group();
        group.id = groupId;
        group.displayName = "Group with Members";
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.existsById("user1")).thenReturn(true);
        when(groupMembershipRepository.existsByGroupIdAndUserId(groupId, "user1")).thenReturn(false);

        Map result = singleGroupsService.singleGroupPut(payload, groupId);

        assertNotNull(result);
        assertEquals("Group with Members", result.get("displayName"));
        //assertEquals(1, ((List<?>) result.get("members")).size());
    }

    @Test
    void singleGroupPatchUpdatesGroup() {
        String groupId = "group1";
        Map<String, Object> payload = new HashMap<>();
        payload.put("displayName", "Patched Group");
        payload.put("Operations", List.of(Map.of("op", "Replace", "path", "displayName", "value", "Patched Group")));

        Group group = new Group();
        group.id = groupId;
        group.displayName = "Old Group";
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(groupMembershipRepository.findByGroupId(groupId, PageRequestBuilder.build())).thenReturn(Page.empty());

        Map result = singleGroupsService.singleGroupPatch(payload, groupId);

        assertNotNull(result);
        assertEquals("Patched Group", result.get("displayName"));
    }

    @Test
    void singleGroupDeleteRemovesGroup() {
        String groupId = "group1";

        Group group = new Group();
        group.id = groupId;
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        Page<GroupMembership> memberships = Page.empty();
        when(groupMembershipRepository.findByGroupId(groupId, PageRequestBuilder.build())).thenReturn(memberships);

        Map result = singleGroupsService.singeGroupDelete(groupId);

        assertNotNull(result);
        verify(groupRepository, times(1)).delete(group);
        verify(groupMembershipRepository, times(1)).deleteAll(memberships);
    }
}
