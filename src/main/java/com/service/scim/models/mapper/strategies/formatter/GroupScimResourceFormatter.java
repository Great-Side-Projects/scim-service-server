package com.service.scim.models.mapper.strategies.formatter;

import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupScimResourceFormatter implements IScimResourceFormatter<Group> {

    @Override
    public Map<String, Object> toScimResource(Group group) {
        HashMap<String, Object> returnValue = new HashMap<>();
        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:schemas:core:2.0:Group");
        returnValue.put("schemas", schemas);
        returnValue.put("id", group.id);
        returnValue.put("displayName", group.displayName);

        // Meta information
        Map<String, Object> meta = new HashMap<>();
        meta.put("resourceType", "Group");
        meta.put("location", ("/scim/v2/Groups/" + group.id));
        returnValue.put("meta", meta);

        if (group.getGroupMemberships() != null) {
            List<Map> gmAL = group.getGroupMemberships()
                    .stream()
                    .map(GroupMembership::toScimResource)
                    .toList();
            returnValue.put("members", gmAL);
        }

        return returnValue;
    }
}
