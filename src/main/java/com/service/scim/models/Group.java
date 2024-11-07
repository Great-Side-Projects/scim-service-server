package com.service.scim.models;

import com.service.scim.models.mapper.AbstractEntityMapper;
import com.service.scim.models.mapper.strategies.formatter.GroupScimResourceFormatter;
import com.service.scim.models.mapper.strategies.formatter.IScimResourceFormatter;
import jakarta.persistence.*;
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

    @Transient
    private IScimResourceFormatter<Group> groupScimResourceFormatter;

    public void setGroupMemberships(List<GroupMembership> groupMemberships) {
        this.groupMemberships = groupMemberships;
    }
    public List<GroupMembership> getGroupMemberships() {
        return this.groupMemberships;
    }

    public Group() {
        this.groupScimResourceFormatter = new GroupScimResourceFormatter();
    }

    public Group(Map<String, Object> resource, AbstractEntityMapper<Group> groupEntityMapper) {
        this.groupScimResourceFormatter = new GroupScimResourceFormatter();
        this.update(resource,groupEntityMapper);
    }

    /**
     * Updates {@link Group} object from JSON {@link Map}
     * @param resource JSON {@link Map} of {@link Group}
     */
    public void update(Map<String, Object> resource, AbstractEntityMapper<Group> groupEntityMapper) {
        try{
            if (resource.containsKey("displayName"))
                this.displayName = resource.get("displayName").toString();
            groupEntityMapper.update(this, resource);
        } catch(Exception e) {
            System.out.println("Error updating fields group: " + e);
        }
    }

    /**
     * Formats JSON {@link Map} response with {@link Group} attributes.
     * @return JSON {@link Map} of {@link Group}
     */
    @Override

    public Map<String, Object> toScimResource() {
        return this.groupScimResourceFormatter.toScimResource(this);
    }
}
