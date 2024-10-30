package com.service.scim.com.service.scim.models.mapper.strategies.patchoperation;

import static org.mockito.Mockito.*;
import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import com.service.scim.models.mapper.strategies.patchoperation.RemoveMembersStrategy;
import com.service.scim.repositories.IGroupMembershipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import java.util.*;

public class RemoveMembersStrategyTest {

    @Mock
    private IGroupMembershipRepository groupMembershipRepository;

    @InjectMocks
    private RemoveMembersStrategy removeMembersStrategy;

    private Group group;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        group = new Group();
        group.id = "group1";
    }

    @Test
    void executeRemovesExistingMembers() {
        List<Map<String, Object>> members = new ArrayList<>();
        Map<String, Object> member = new HashMap<>();
        member.put("value", "user1");
        members.add(member);

        GroupMembership membership = new GroupMembership();
        membership.id ="user1";
        membership.groupId = "group1";

        when(groupMembershipRepository.existsByGroupIdAndUserId(anyString(), anyString())).thenReturn(true);
        when(groupMembershipRepository.findByGroupIdAndUserId(anyString(), anyString(), any())).thenReturn(new PageImpl<>(List.of(membership)));

        removeMembersStrategy.execute(group, members);

        verify(groupMembershipRepository, times(1)).deleteAll(anyList());
    }

    @Test
    void executeIgnoresNonExistingMembers() {
        List<Map<String, Object>> members = new ArrayList<>();
        Map<String, Object> member = new HashMap<>();
        member.put("value", "user1");
        members.add(member);

        when(groupMembershipRepository.existsByGroupIdAndUserId(anyString(), anyString())).thenReturn(false);

        removeMembersStrategy.execute(group, members);

        verify(groupMembershipRepository, never()).deleteAll(anyList());
    }

    @Test
    void executeHandlesEmptyMembersList() {
        List<Map<String, Object>> members = new ArrayList<>();

        removeMembersStrategy.execute(group, members);

        verify(groupMembershipRepository, never()).deleteAll(anyList());
    }

    @Test
    void executeHandlesNullMembersList() {
        removeMembersStrategy.execute(group, null);

        verify(groupMembershipRepository, never()).deleteAll(anyList());
    }
}
