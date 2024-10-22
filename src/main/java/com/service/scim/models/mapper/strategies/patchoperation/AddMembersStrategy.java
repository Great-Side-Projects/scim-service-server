package com.service.scim.models.mapper.strategies.patchoperation;

import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.repositories.IUserRepository;

import java.util.*;

public class AddMembersStrategy implements IPatchOperationStrategy<Group> {
    private final IGroupMembershipRepository groupMembershipRepository;
    private final IUserRepository userRepository;

    public AddMembersStrategy(IGroupMembershipRepository groupMembershipRepository, IUserRepository userRepository) {
        this.groupMembershipRepository = groupMembershipRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(Group entity, List<Map<String, Object>> members) {
        List<String> userIds = members.stream()
                .map(member -> member.get("value").toString())
                .toList();

        Map<String, String> userNames = userRepository.findUserNamesByIdsMap(userIds);
        List<GroupMembership> membershipsToAdd = createMemberships(entity, members, userNames);

        if (!membershipsToAdd.isEmpty()) {
            groupMembershipRepository.saveAll(membershipsToAdd);
        }
    }

    private List<GroupMembership> createMemberships(Group group, List<Map<String, Object>> members, Map<String, String> userNames) {
        List<GroupMembership> membershipsToAdd = new ArrayList<>();
        members.forEach(member -> {
            String userId = member.get("value").toString();
            if (!groupMembershipRepository.existsByGroupIdAndUserId(group.id, userId) && userRepository.existsById(userId)) {
                //GroupMembership membership = createMembership(group, userId, userNames.get(userId));
                GroupMembership membership = new GroupMembership(member, group.id, group.displayName);
                membership.userDisplay = userNames.get(userId);
                membershipsToAdd.add(membership);
            }
        });
        return membershipsToAdd;
    }
}
