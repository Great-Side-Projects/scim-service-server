package com.service.scim.com.service.scim.models.mapper.strategies.patchoperation;

import static org.mockito.Mockito.*;
import com.service.scim.models.Group;
import com.service.scim.models.mapper.strategies.patchoperation.AddMembersStrategy;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.repositories.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class AddMembersStrategyTest {

    @Mock
    private IGroupMembershipRepository groupMembershipRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private AddMembersStrategy addMembersStrategy;

    private Group group;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        group = new Group();
        group.id = "group1";
        group.displayName = "Test Group";
    }

    @Test
    void executeAddsNewMembers() {
        List<Map<String, Object>> members = new ArrayList<>();
        Map<String, Object> member = new HashMap<>();
        member.put("value", "user1");
        members.add(member);

        when(userRepository.findUserNamesByIdsMap(anyList())).thenReturn(Map.of("user1", "User One"));
        when(groupMembershipRepository.existsByGroupIdAndUserId(anyString(), anyString())).thenReturn(false);
        when(userRepository.existsById(anyString())).thenReturn(true);

        addMembersStrategy.execute(group, members);

        verify(groupMembershipRepository, times(1)).saveAll(anyList());
    }

    @Test
    void executeIgnoresExistingMembers() {
        List<Map<String, Object>> members = new ArrayList<>();
        Map<String, Object> member = new HashMap<>();
        member.put("value", "user1");
        members.add(member);

        when(userRepository.findUserNamesByIdsMap(anyList())).thenReturn(Map.of("user1", "User One"));
        when(groupMembershipRepository.existsByGroupIdAndUserId(anyString(), anyString())).thenReturn(true);

        addMembersStrategy.execute(group, members);

        verify(groupMembershipRepository, never()).saveAll(anyList());
    }

    @Test
    void executeIgnoresNonExistentUsers() {
        List<Map<String, Object>> members = new ArrayList<>();
        Map<String, Object> member = new HashMap<>();
        member.put("value", "user1");
        members.add(member);

        when(userRepository.findUserNamesByIdsMap(anyList())).thenReturn(Map.of("user1", "User One"));
        when(groupMembershipRepository.existsByGroupIdAndUserId(anyString(), anyString())).thenReturn(false);
        when(userRepository.existsById(anyString())).thenReturn(false);

        addMembersStrategy.execute(group, members);

        verify(groupMembershipRepository, never()).saveAll(anyList());
    }

    @Test
    void executeHandlesEmptyMembersList() {
        List<Map<String, Object>> members = new ArrayList<>();

        addMembersStrategy.execute(group, members);

        verify(groupMembershipRepository, never()).saveAll(anyList());
    }

    @Test
    void executeHandlesNullMembersList() {
        addMembersStrategy.execute(group, null);

        verify(groupMembershipRepository, never()).saveAll(anyList());
    }
}