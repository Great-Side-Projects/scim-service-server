/** Copyright © 2018, Okta, Inc.
 *
 *  Licensed under the MIT license, the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     https://opensource.org/licenses/MIT
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.service.scim.models;

import com.service.scim.triggers.UserTrailListener;
import javax.persistence.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

/**
 * Database schema for {@link User}
 */
@Entity @EntityListeners(UserTrailListener.class)
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
    @Column(unique=true, nullable=false, length=250)
    public String userName;

    /**
     * The email of the user
     * Non-nullable, unique
     * Max length: 250
     */
    @Column(nullable=false, length=250)
    public String email;

    /**
     * The last name (family name) of the user
     * Max length: 250
     */
    @Column(length=250)
    public String familyName;

    /**
     * The middle name of the user
     * Max length: 250
     */
    @Column(length=250)
    public String middleName;

    /**
     * The first name (given name) of the user
     * Max length: 250
     */
    @Column(length=250)
    public String givenName;

    /**
     * The title of the user
     * Max length: 250
     */
    @Column(length=250)
    public String title;

    /**
     * The displayName of the user
     * Max length: 250
     */
    @Column(length=250)
    public String displayName;

    /**
     * The locale of the user
     * Max length: 250
     */
    @Column(length=250)
    public String locale;

    /**
     * The nickName of the user
     * Max length: 250
     */
    @Column(length=250)
    public String nickName;

    /**
     * The profileUrl of the user
     * Max length: 250
     */
    @Column(length=250)
    public String profileUrl;

    /**
     * The secondEmail of the user
     * Max length: 250
     */
    @Column(length=250)
    public String secondEmail;

    /**
     * The mobilePhone of the user
     * Max length: 250
     */
    @Column(length=250)
    public String mobilePhone;

    /**
     * The primaryPhone of the user
     * Max length: 250
     */
    @Column(length=250)
    public String primaryPhone;

    /**
     * The Street Address of the user
     * Max length: 250
     */
    @Column(length=250)
    public String streetAddress;

    /**
     * The city of the user
     * Max length: 250
     */
    @Column(length=250)
    public String city;

    /**
     * The state or province of the user
     * Max length: 250
     */
    @Column(length=250)
    public String state;

    /**
     * The organization of the user
     * Max length: 250
     */
    @Column(length=250)
    public String organization;

    /**
     * The division of the user
     * Max length: 250
     */
    @Column(length=250)
    public String division;

    /**
     * The manager of the user
     * Max length: 250
     */
    @Column(length=250)
    public String manager;


    /**
     * The department of the user
     * Max length: 250
     */
    @Column(length=250)
    public String department;

    /**
     * The Office location of the user
     * Max length: 250
     */
    @Column(length=250)
    public String officeLocation;

    /**
     * The country of the user
     * Max length: 250
     */
    @Column(length=250)
    public String country;

    /**
     * The Business phone of the user
     * Max length: 250
     */
    @Column(length=250)
    public String businessphone;

    /**
     * The Employee Number phone of the user
     * Max length: 250
     */
    @Column(length=250)
    public String employeeNumber;

    /**
     * The Postal Code of the user
     * Max length: 250
     */
    @Column(length=250)
    public String postalCode;

    private static Map<String, String> mappedProperty = new HashMap<>();;

    public boolean IsMappedProperty(String key){
        return this.mappedProperty.containsKey(key);
    }
    public String getMappedProperty(String Key){
        return  mappedProperty.get(Key);
    }

    public Boolean getStatusChanged() {
        return this.statusChanged;
    }

    @Transient
    private Boolean statusChanged = false;

    public User() {
        InitmappedProperty();
    }

    private static void InitmappedProperty() {
        mappedProperty.put("addresses[type eq \"work\"].formatted","officeLocation");
        mappedProperty.put("addresses[type eq \"work\"].streetAddress","streetAddress");
        mappedProperty.put("addresses[type eq \"work\"].locality","city");
        mappedProperty.put("addresses[type eq \"work\"].postalCode","postalCode");
        mappedProperty.put("addresses[type eq \"work\"].country","country");
        mappedProperty.put("phoneNumbers[type eq \"work\"].value","businessphone");
        mappedProperty.put("phoneNumbers[type eq \"mobile\"].value","mobilePhone");
        mappedProperty.put("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:employeeNumber","employeeNumber");
        mappedProperty.put("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:department","department");
        mappedProperty.put("addresses[type eq \"work\"].region","state");
    }

    public User(Map<String, Object> resource){
        this.update(resource);
    }

    /**
     * Updates {@link User} object from JSON {@link Map}
     * @param resource JSON {@link Map} of {@link User}
     */
    public void update(Map<String, Object> resource) {
        try{
            Map<String, Object> names = (Map<String, Object>)resource.get("name");

            for(String subName : names.keySet()){
                switch (subName) {
                    case "givenName":
                        this.givenName = getMapValue(names, subName);
                        break;
                    case "familyName":
                        this.familyName = getMapValue(names, subName);
                        break;
                    case "middleName":
                        this.middleName = getMapValue(names, subName);
                        break;
                    default:
                        break;
                }
            }

          ((ArrayList)resource.get("emails")).forEach( email -> {
                Map<String, Object>  e = (Map<String, Object>)email;
                if (((Boolean) e.get("primary")))
                    this.email = getMapValue(e, "value");
            });

          if(resource.containsKey("phoneNumbers")) {
              ((ArrayList) resource.get("phoneNumbers")).forEach(phoneNumber -> {
                  Map<String, Object> phone = (Map<String, Object>) phoneNumber;
                  if (((Boolean) phone.get("primary")))
                      this.primaryPhone = getMapValue(phone, "value");
                  if (phone.get("type").equals("mobile"))
                      this.mobilePhone = getMapValue(phone, "value");
                  if (phone.get("type").equals("work"))
                      this.businessphone = getMapValue(phone, "value");
              });
          }

          if(resource.containsKey("addresses")) {
              ((ArrayList) resource.get("addresses")).forEach(address -> {
                  Map<String, Object> a = (Map<String, Object>) address;
                  //if (((Boolean) a.get("primary")))
                  this.streetAddress = getMapValue(a, "streetAddress");
                  this.country = getMapValue(a, "country");
                  this.city = getMapValue(a, "locality");
                  this.postalCode = getMapValue(a, "postalCode");
                  this.state = getMapValue(a, "region");
                  return;
              });
          }

          Map<String, Object> userComapanyData =  resource.containsKey("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User") ? (Map<String, Object>)resource.get("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User") : new HashMap<>();

          if (containsKeyMapValue(userComapanyData,"country"))
              this.country = getMapValue(userComapanyData,"country");
          if (containsKeyMapValue(userComapanyData,"postalCode"))
              this.postalCode = getMapValue(userComapanyData,"postalCode");

          this.department = getMapValue(userComapanyData,"department");
          this.division = getMapValue(userComapanyData,"division");
          this.employeeNumber = getMapValue(userComapanyData,"employeeNumber");

          this.organization = getMapValue(userComapanyData,"organization");

          if (containsKeyMapValue(userComapanyData,"manager"))
              this.manager = getMapValue((Map<String, Object>)userComapanyData.get("manager") ,"displayName");

          this.userName = resource.get("userName").toString();
          this.setActive((Boolean)resource.get("active"));
          this.title = getMapValue(resource, "title");
          this.displayName = getMapValue(resource, "displayName");
          this.nickName = getMapValue(resource, "nickName");
          this.profileUrl = getMapValue(resource, "profileUrl");
          this.secondEmail = getMapValue(resource, "secondEmail");
          this.locale = getMapValue(resource, "locale");
/*
          if (containsKeyMapValue(resource,"mobilePhone"))
              this.mobilePhone = getMapValue(resource, "mobilePhone");
          if (containsKeyMapValue(resource,"state"))
              this.state = getMapValue(resource, "state");
          if (containsKeyMapValue(resource,"businessphone"))
              this.businessphone = getMapValue(resource, "businessphone");
          if (containsKeyMapValue(resource,"streetAddress"))
              this.streetAddress = getMapValue(resource, "streetAddress");
          if (containsKeyMapValue(resource,"city"))
              this.city = getMapValue(resource, "city");
*/
          this.officeLocation = getMapValue(userComapanyData,"officeLocation");

        } catch(Exception e) {
             System.out.println(e);
        }
    }

    private static String getMapValue(Map<String, Object> resource, String key) {
      return resource.get(key) == null ? null : resource.get(key).toString();
    }

    private static Boolean containsKeyMapValue(Map<String, Object> resource, String key) {
        return resource.containsKey(key);
    }

    /**
     * Formats JSON {@link Map} response with {@link User} attributes
     * @return JSON {@link Map} of {@link User}
     */
    @Override
    public Map toScimResource(){
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
