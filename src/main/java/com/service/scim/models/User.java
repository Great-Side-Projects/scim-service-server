package com.service.scim.models;

import com.service.scim.models.mapper.AbstractEntityMapper;
import com.service.scim.models.mapper.strategies.formatter.IScimResourceFormatter;
import com.service.scim.models.mapper.strategies.formatter.UserScimResourceFormatter;
import com.service.scim.triggers.UserListener;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Database schema for {@link User}
 */
@Entity
@EntityListeners(UserListener.class)
@Table(name = "users")
public class User extends BaseModel {
    /**
     * The unique identifier of the user
     * UUID4 following the RFC 7643 requirement
     */
    @Transient
    private IScimResourceFormatter<User> userScimResourceFormatter;

    @Column(length = 36)
    @Id
    public String id;

    public String getId() {
        return this.id;
    }

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

    public String getUserName() {
        return this.userName;
    }

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

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime createdAt = LocalDateTime.now(ZoneOffset.UTC);

    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime updatedAt;

    public User() {
        this.userScimResourceFormatter = new UserScimResourceFormatter();
    }

    public User(Map<String, Object> resource, AbstractEntityMapper<User> userEntityMapper) {
        this.userScimResourceFormatter = new UserScimResourceFormatter();
        this.update(resource, userEntityMapper);
    }

    /**
     * Updates {@link User} object from JSON {@link Map}
     *
     * @param resource JSON {@link Map} of {@link User}
     */
    public void update(Map<String, Object> resource, AbstractEntityMapper<User> userEntityMapper) {
        try {

            if (resource.containsKey("active")) {
                this.setActive((Boolean) resource.get("active"));
                resource.remove("active");
            }

            userEntityMapper.update(this, resource);
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
        return this.userScimResourceFormatter.toScimResource(this);
    }
}
