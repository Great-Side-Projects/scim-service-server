package com.service.scim.models;

import com.service.scim.models.mapper.UserMapper;
import com.service.scim.triggers.UserTrailListener;
import jakarta.persistence.*;
import java.util.*;

/**
 * Database schema for {@link User}
 */
@Entity
@EntityListeners(UserTrailListener.class)
@Table(name = "users")
public class User extends BaseModel {
    /**
     * The unique identifier of the user
     * UUID4 following the RFC 7643 requirement
     */
    @Column(length = 36)
    @Id
    public String id;

    public void setActive(Boolean value) {
        if (value == null) {
            return;
        }
        if (!Objects.equals(this.active, value)) {
            this.statusChanged = true;
            this.active = value;
        }
    }

    /**
     * The active status of the user
     * Default: False
     */
    @Column(columnDefinition = "boolean default false")
    public Boolean active = false;

    /**
     * The username of the user
     * Non-nullable, unique
     * Max length: 250
     */
    @Column(unique = true, nullable = false, length = 250)
    public String userName;

    /**
     * The email of the user
     * Non-nullable, unique
     * Max length: 250
     */
    @Column(nullable = false, length = 250)
    public String email;

    /**
     * The last name (family name) of the user
     * Max length: 250
     */
    @Column(nullable = false, length = 250)
    public String familyName;

    /**
     * The middle name of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String middleName;

    /**
     * The first name (given name) of the user
     * Max length: 250
     */
    @Column(nullable = false, length = 250)
    public String givenName;

    /**
     * The title of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String title;

    /**
     * The displayName of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String displayName;

    /**
     * The locale of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String locale;

    /**
     * The nickName of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String nickName;

    /**
     * The profileUrl of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String profileUrl;

    /**
     * The secondEmail of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String secondEmail;

    /**
     * The mobilePhone of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String mobilePhone;

    /**
     * The primaryPhone of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String primaryPhone;

    /**
     * The Street Address of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String streetAddress;

    /**
     * The city of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String locality;

    /**
     * The state or province of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String region;

    /**
     * The organization of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String organization;

    /**
     * The division of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String division;

    /**
     * The manager of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String manager;


    /**
     * The department of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String department;

    /**
     * The Office location of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String postalAddress;

    /**
     * The country of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String country;

    /**
     * The Business phone of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String businessPhone;

    /**
     * The Employee Number phone of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String employeeNumber;

    /**
     * The Postal Code of the user
     * Max length: 250
     */
    @Column(length = 250)
    public String postalCode;

    public Boolean getStatusChanged() {
        return this.statusChanged;
    }

    @Transient
    private Boolean statusChanged = false;

    public User() {
    }

    public User(Map<String, Object> resource, UserMapper userMapper) {
        this.update(resource, userMapper);
    }

    /**
     * Updates {@link User} object from JSON {@link Map}
     *
     * @param resource JSON {@link Map} of {@link User}
     */
    public void update(Map<String, Object> resource, UserMapper userMapper) {
        try {

            if (resource.containsKey("active")) {
                this.setActive((Boolean) resource.get("active"));
                resource.remove("active");
            }

            userMapper.update(this, resource);
        } catch (Exception e) {
            //TODO: check exception
            System.out.println(e);
        }
    }

    /**
     * Formats JSON {@link Map} response with {@link User} attributes
     *
     * @return JSON {@link Map} of {@link User}
     */
    @Override
    public Map toScimResource() {
        Map<String, Object> returnValue = new HashMap<>();
        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:schemas:core:2.0:User");
        returnValue.put("schemas", schemas);
        returnValue.put("id", this.id);
        returnValue.put("active", this.active);
        returnValue.put("userName", this.userName);

        // Name
        Map<String, Object> names = new HashMap<>();
        names.put("familyName", this.familyName);
        names.put("givenName", this.givenName);
        names.put("middleName", this.middleName);
        names.put("displayName", this.displayName);
        returnValue.put("name", names);

        // Meta information
        Map<String, Object> meta = new HashMap<>();
        meta.put("resourceType", "User");
        meta.put("location", ("/scim/v2/Users/" + this.id));
        returnValue.put("meta", meta);

        List<Map<String, Object>> emails = new ArrayList<>();
        Map<String, Object> primaryEmail = new HashMap<>();
        primaryEmail.put("primary", true);
        primaryEmail.put("value", email);
        primaryEmail.put("type", "work");
        emails.add(primaryEmail);
        returnValue.put("emails", emails);

        return returnValue;
    }
}
