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

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Database schema for {@link User}
 */
@Entity
@Table(name = "users")
public class User extends BaseModel {
    /**
     * The unique identifier of the user
     * UUID4 following the RFC 7643 requirement
     */
    @Column(length = 36)
    @Id
    public String id;

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

    public User() {}

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

            Map<String, Object> email;
            if (!((ArrayList) resource.get("emails")).isEmpty())
            {
                email = ((Map<String, Object>)((ArrayList) resource.get("emails")).get(0));
                names.put("email",email.get("value"));
            }

            for(String subName : names.keySet()){
                switch (subName) {
                    case "givenName":
                        this.givenName = names.get(subName) == null ? null : names.get(subName).toString();
                        break;
                    case "familyName":
                        this.familyName = names.get(subName) == null ? null : names.get(subName).toString();
                        break;
                    case "middleName":
                        this.middleName = names.get(subName) == null ? null : names.get(subName).toString();
                        break;
                    case "email":
                        this.email = names.get(subName) == null ? null : names.get(subName).toString();
                        break;
                    default:
                        break;
                }
            }
          this.userName = resource.get("userName").toString();
          this.active = (Boolean)resource.get("active");
        } catch(Exception e) {
             System.out.println(e);
        }
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
        primaryEmail.put("value", userName);
        primaryEmail.put("type", "work");
        emails.add(primaryEmail);
        returnValue.put("emails", emails);

        return returnValue;
    }
}
