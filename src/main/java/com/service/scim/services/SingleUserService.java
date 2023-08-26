package com.service.scim.services;

import com.service.scim.database.UserDatabase;
import com.service.scim.models.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.service.scim.utils.SCIM.*;

@Service
public class SingleUserService implements ISingleUserService {

    private final UserDatabase db;

    public SingleUserService(UserDatabase db) {
        this.db = db;
    }

    @Override
    public Map singeUserGet(String id, HttpServletResponse response) {
        try {
            //System.out.println(headers);
            User user = db.findById(id).get(0);
            return user.toScimResource();

        } catch (Exception e) {
            response.setStatus(404);
            return scimError(USER_NOT_FOUND_MSG, Optional.of(404));
        }
    }

    @Override
    public Map singleUserPut(Map<String, Object> payload, String id) {
        User user = db.findById(id).get(0);
        user.update(payload);
        db.save(user);
        return user.toScimResource();
    }

    @Override
    public Map singleUserPatch(Map<String, Object> payload, String id) {
        List schema = (List)payload.get("schemas");
        List<Map> operations = (List)payload.get("Operations");

        if(schema == null){
            return scimError(SCHEMA_ERROR_MSG, Optional.of(400));
        }
        if(operations == null){
            return scimError(OPERATIONS_ERROR_MSG, Optional.of(400));
        }

        //Verify schema
        String schemaPatchOp = "urn:ietf:params:scim:api:messages:2.0:PatchOp";
        if (!schema.contains(schemaPatchOp)){
            return scimError(SCHEMA_NOT_SUPPORTED_MSG, Optional.of(501));
        }

        int found = db.findById(id).size();

        if (found == 0) {
            return scimError(String.format(USER_NOT_FOUND, id), Optional.of(404));
        }

        //Find user for update
        User user = db.findById(id).get(0);

        for(Map map : operations){

            String key = map.get("path").toString();
            String value = map.get("value").toString();

            switch (key) {
                case "active":
                    user.setActive(Boolean.parseBoolean(value));
                    break;
                default:
                    user.setProperty(key, value);
                    break;
            }
        }

        db.save(user);

        return user.toScimResource();
    }
}
