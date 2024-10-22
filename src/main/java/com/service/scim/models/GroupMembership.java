package com.service.scim.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Database schema for {@link GroupMembership}
 */
@Entity
@Table(name = "groupmemberships")
public class GroupMembership extends BaseModel {
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

    public GroupMembership() {}

    public GroupMembership(Map<String, Object> resource, String groupId, String groupDisplay){
        this.update(resource);
        this.groupId = groupId;
        this.id = UUID.randomUUID().toString();
        this.groupDisplay = groupDisplay;
    }

    /**
     * Updates {@link GroupMembership} object from JSON {@link Map}
     * @param resource JSON {@link Map} of {@link GroupMembership}
     */
    public void update(Map<String, Object> resource) {
        try{
            this.userId = resource.get("value").toString();
            this.userDisplay = resource.getOrDefault("display", "").toString();
        } catch(Exception e) {
            System.out.println("Error updating fields groupmembership: " + e);
        }
    }

    /**
     * Formats JSON {@link Map} response with {@link Group} attributes.
     * @return JSON {@link Map} of {@link Group}
     */
    @Override
    public Map toScimResource(){
        Map<String, Object> returnValue = new HashMap<>();

        returnValue.put("value", this.userId);
        returnValue.put("display", this.userDisplay);

        return returnValue;
    }

    public Map toUserScimResource() {
        Map<String, Object> returnValue = new HashMap<>();

        returnValue.put("value", this.groupId);
        returnValue.put("display", this.groupDisplay);

        return returnValue;
    }
}
