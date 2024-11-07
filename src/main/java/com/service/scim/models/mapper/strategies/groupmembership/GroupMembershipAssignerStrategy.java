package com.service.scim.models.mapper.strategies.groupmembership;

import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupMembershipAssignerStrategy implements IGroupMembershipAssigner<Group> {

    private final List<GroupMembership> groupMemberships;

    public GroupMembershipAssignerStrategy(List<GroupMembership> groupMemberships) {
        this.groupMemberships = groupMemberships;
    }
    @Override
    public List<Map<String, Object>> assignMemberships(List<Map<String, Object>> resources, List<Group> groups) {
        return resources.stream().map(resource -> {
            String groupId = (String) resource.get("id");
            List memberships = groupMemberships.stream()
                    .filter(gm -> gm.groupId.equals(groupId))
                    .map(GroupMembership::toScimResource)
                    .collect(Collectors.toList());
            resource.put("members", memberships);
            return resource;
        }).collect(Collectors.toList());
    }
}

