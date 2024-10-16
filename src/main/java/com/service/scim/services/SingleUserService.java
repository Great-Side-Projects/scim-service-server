package com.service.scim.services;

import com.service.scim.models.UserMapper;
import com.service.scim.repositories.UserDatabase;
import com.service.scim.models.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import java.util.*;
import static com.service.scim.utils.SCIM.*;

@Service
public class SingleUserService implements ISingleUserService {

    private final UserDatabase db;
    private final UserMapper userMapper;

    public SingleUserService(UserDatabase db, UserMapper userMapper) {
        this.db = db;
        this.userMapper = userMapper;
    }

    @Override
    public Map singeUserGet(String id, HttpServletResponse response) {
        try {
            //System.out.println(headers);
            User user = db.findById(id).getFirst();
            return user.toScimResource();

        } catch (Exception e) {
            response.setStatus(404);
            return scimError(USER_NOT_FOUND_MSG, Optional.of(404));
        }
    }

    @Override
    public Map singleUserPut(Map<String, Object> payload, String id) {
        User user = db.findById(id).getFirst();
        user.update(payload, userMapper);
        db.save(user);
        return user.toScimResource();
    }

    @Override
    public Map singleUserPatch(Map<String, Object> payload, String id) {
        List schema = (List)payload.get("schemas");
        List<Map> operations = (List)payload.get("Operations");

        Map<String, Object> userMapOperations = operations.
                stream().
                collect(HashMap::new, (m, v) ->
                        m.put(v.get("path").toString(), v.get("value").toString()), HashMap::putAll
                );

        if(schema == null){
            return scimError(SCHEMA_ERROR_MSG, Optional.of(400));
        }
        if(userMapOperations == null){
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
        User user = db.findById(id).getFirst();
        user.update(userMapOperations, userMapper);
         db.save(user);

        return user.toScimResource();
    }
}
