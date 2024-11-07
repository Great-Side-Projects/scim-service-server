package com.service.scim.models;

import com.service.scim.models.mapper.strategies.formatter.GroupMembershipScimResourceFormatter;
import jakarta.persistence.*;
import java.util.Map;
import java.util.UUID;

/**
 * Database schema for {@link GroupMembership}
 */
@Entity
@Table(name = "groupmemberships")
public class GroupMembership extends BaseModel {

    @Transient
    private GroupMembershipScimResourceFormatter scimResourceFormatter;
    /**
     * The unique identifier of the object
     * UUID4 following the RFC 7643 requirement
     */
    @Column(length = 36)
    @Id
    public String id;

    @Column(nullable = false, length = 36)
    public String groupId;

    @Column(nullable = false, length = 36)
    public String userId;

    @Column
    public String groupDisplay;

    @Column
    public String userDisplay;

    public GroupMembership(Map<String, Object> resource, String groupId, String groupDisplay) {
        this.update(resource);
        this.groupId = groupId;
        this.id = UUID.randomUUID().toString();
        this.groupDisplay = groupDisplay;
        this.scimResourceFormatter = new GroupMembershipScimResourceFormatter();
    }

    public GroupMembership() {
        this.scimResourceFormatter = new GroupMembershipScimResourceFormatter();
    }

    /**
     * Updates {@link GroupMembership} object from JSON {@link Map}
     *
     * @param resource JSON {@link Map} of {@link GroupMembership}
     */
    public void update(Map<String, Object> resource) {
        try {
            this.userId = resource.get("value").toString();
            this.userDisplay = resource.getOrDefault("display", "").toString();
        } catch (Exception e) {
            System.out.println("Error updating fields groupmembership: " + e);
        }
    }

    /**
     * Formats JSON {@link Map} response with {@link Group} attributes.
     *
     * @return JSON {@link Map} of {@link Group}
     */
    @Override
    public Map toScimResource() {
        return this.scimResourceFormatter.toScimResource(this);
    }

    public Map toUserScimResource() {
        return this.scimResourceFormatter.toUserScimResource(this);
    }
}
