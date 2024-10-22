package com.service.scim.models;

import com.service.scim.models.mapper.AbstractEntityMapper;
import com.service.scim.models.mapper.GroupEntityMapper;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database schema for {@link Group}
 */
@Entity
@Table(name = "`groups`")
public class Group extends BaseModel {
    /**
     * The unique identifier of the object
     * UUID4 following the RFC 7643 requirement
     */
    @Column(length = 36)
    @Id
    public String id;

    /**
     * The userDisplay name of the group
     * Non-nullable
     * Max length: 250
     */
    @Column(nullable = false, length = 250)
    public String displayName;

    @Transient
    private List<GroupMembership> groupMemberships;

    public void setGroupMemberships(List<GroupMembership> groupMemberships) {
        this.groupMemberships = groupMemberships;
    }

    public Group() {}

    public Group(Map<String, Object> resource, AbstractEntityMapper groupEntityMapper) {
        this.update(resource,groupEntityMapper);
    }

    /**
     * Updates {@link Group} object from JSON {@link Map}
     * @param resource JSON {@link Map} of {@link Group}
     */
    public void update(Map<String, Object> resource, AbstractEntityMapper groupEntityMapper) {
        try{
            if (resource.containsKey("displayName"))
                this.displayName = resource.get("displayName").toString();
            groupEntityMapper.update(this, resource);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Formats JSON {@link Map} response with {@link Group} attributes.
     * @return JSON {@link Map} of {@link Group}
     */
    @Override
    public HashMap<String, Object> toScimResource() {
        HashMap<String, Object> returnValue = new HashMap<>();
        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:schemas:core:2.0:Group");
        returnValue.put("schemas", schemas);
        returnValue.put("id", this.id);
        returnValue.put("displayName", this.displayName);

        // Meta information
        Map<String, Object> meta = new HashMap<>();
        meta.put("resourceType", "Group");
        meta.put("location", ("/scim/v2/Groups/" + this.id));
        returnValue.put("meta", meta);

        if (this.groupMemberships != null) {
            List<Map> gmAL = this.groupMemberships
                    .stream()
                    .map(GroupMembership::toScimResource)
                    .toList();
            returnValue.put("members", gmAL);
        }

        return returnValue;
    }
}
