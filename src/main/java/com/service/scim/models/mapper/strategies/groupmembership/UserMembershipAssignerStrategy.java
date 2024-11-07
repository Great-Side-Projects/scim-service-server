package com.service.scim.models.mapper.strategies.groupmembership;

import com.service.scim.models.GroupMembership;
import com.service.scim.models.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserMembershipAssignerStrategy implements IGroupMembershipAssigner<User> {

    private final List<GroupMembership> groupMemberships;

    public UserMembershipAssignerStrategy(List<GroupMembership> groupMemberships) {
        this.groupMemberships = groupMemberships;
    }

    @Override
    public List<Map<String, Object>> assignMemberships(List<Map<String, Object>> resources, List<User> users) {
        return resources.stream().map(resource -> {
            String userId = (String) resource.get("id");
            List memberships = groupMemberships.stream()
                    .filter(gm -> gm.userId.equals(userId))
                    .map(GroupMembership::toUserScimResource)
                    .collect(Collectors.toList());
            resource.put("groups", memberships);
            return resource;
        }).collect(Collectors.toList());
    }
}