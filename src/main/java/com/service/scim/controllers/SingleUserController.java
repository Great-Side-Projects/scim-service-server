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

package com.service.scim.controllers;

import com.service.scim.database.UserDatabase;
import com.service.scim.models.User;
import org.hibernate.mapping.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.lang.reflect.Field;
import java.security.KeyPair;
import java.util.*;

/**
 *  URL route (root)/scim/v2/Users/{id}
 */
@Controller
@RequestMapping("/scim/v2/Users/{id}")
@CrossOrigin("*")
public class SingleUserController {
    UserDatabase db;

    @Autowired
    public SingleUserController(UserDatabase db) {
        this.db = db;
    }

    /**
     * Queries database for {@link User} with identifier
     * Updates response code with '404' if unable to locate {@link User}
     * @param id {@link User#id}
     * @param response HTTP Response
     * @return {@link #scimError(String, Optional)} / JSON {@link Map} of {@link User}
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Map singeUserGet(@PathVariable String id,  HttpServletResponse response,
                                          @RequestHeader(required = false) Map<String, String> headers) {

        try {
            //System.out.println(headers);
            User user = db.findById(id).get(0);
            return user.toScimResource();

        } catch (Exception e) {
            response.setStatus(404);
            return scimError("User not found", Optional.of(404));
        }
    }

    /**
     * Update via Put {@link User} attributes
     * @param payload Payload from HTTP request
     * @param id {@link User#id}
     * @return JSON {@link Map} of {@link User}
     */
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody Map singleUserPut(@RequestBody Map<String, Object> payload,
                                           @PathVariable String id,
                                           @RequestHeader(required = false) Map<String, String> headers) {

        //System.out.println(headers);
        User user = db.findById(id).get(0);
        user.update(payload);
        db.save(user);
        return user.toScimResource();
    }


    void propertyChangeActive(PropertyChangeEvent evt){
        //try {
            System.out.println("valores diferentes");
         // Thread.sleep(10000);
        //} catch (InterruptedException e) {
            //    throw new RuntimeException(e);
        //}

    }

    /**
     * Update via Patch {@link User} attributes
     * @param payload Payload from HTTP request
     * @param id {@link User#id}
     * @return {@link #scimError(String, Optional)} / JSON {@link Map} of {@link User}
     */
    @RequestMapping(method = RequestMethod.PATCH)
    public @ResponseBody Map singleUserPatch(@RequestBody Map<String, Object> payload,
                                             @PathVariable String id) {
        List schema = (List)payload.get("schemas");
        List<Map> operations = (List)payload.get("Operations");

        if(schema == null){
            return scimError("Payload must contain schema attribute.", Optional.of(400));
        }
        if(operations == null){
            return scimError("Payload must contain operations attribute.", Optional.of(400));
        }

        //Verify schema
        String schemaPatchOp = "urn:ietf:params:scim:api:messages:2.0:PatchOp";
        if (!schema.contains(schemaPatchOp)){
            return scimError("The 'schemas' type in this request is not supported.", Optional.of(501));
        }

        int found = db.findById(id).size();

        if (found == 0) {
            return scimError("User '" + id + "' was not found.", Optional.of(404));
        }

        //Find user for update
        User user = db.findById(id).get(0);

        Map<String, Object> values = new HashMap();
        for(Map map : operations){

            String key = map.get("path").toString();
            String value = map.get("value").toString();

            switch (key) {
                case "active":
                    //values.put(key, Boolean.parseBoolean(value));
                    user.setActive(Boolean.parseBoolean(value));
                    break;
                default:
                    key = user.IsMappedProperty(key) ? user.getMappedProperty(key) : key;
                    values.put(key, value);
                    break;
            }
        }

        // Use Java reflection to find and set User attribute
        for (Map.Entry key : values.entrySet()) {
            try {
                Field field = user.getClass().getDeclaredField(key.getKey().toString());
                field.set(user, key.getValue());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Error - Do not update field
            }
        }

        db.save(user);

        return user.toScimResource();
    }

    /**
     * Output custom error message with response code
     * @param message Scim error message
     * @param status_code Response status code
     * @return JSON {@link Map} of {@link User}
     */
    public Map scimError(String message, Optional<Integer> status_code){

        Map<String, Object> returnValue = new HashMap<>();
        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:api:messages:2.0:Error");
        returnValue.put("schemas", schemas);
        returnValue.put("detail", message);

        // Set default to 500
        returnValue.put("status", status_code.orElse(500));
        return returnValue;
    }
}
