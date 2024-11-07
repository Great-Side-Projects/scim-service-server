package com.service.scim.models.mapper.strategies.formatter;

import com.service.scim.models.GroupMembership;
import java.util.Map;

public class GroupMembershipScimResourceFormatter implements IScimResourceFormatter<GroupMembership> {

    @Override
    public Map<String, Object> toScimResource(GroupMembership groupMembership) {

        return Map.of(
                "value", groupMembership.userId == null ? "" : groupMembership.userId,
                "display", groupMembership.userDisplay == null ? "" : groupMembership.userDisplay
        );
    }

    public Map<String, Object> toUserScimResource(GroupMembership groupMembership) {

        return Map.of(
                "value", groupMembership.groupId == null ? "" : groupMembership.groupId,
                "display", groupMembership.groupDisplay == null ? "" : groupMembership.groupDisplay
        );
    }
}
