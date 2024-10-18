package com.service.scim.services;

import com.service.scim.models.mapper.UserMapper;
import com.service.scim.repositories.IUserRepository;
import com.service.scim.models.User;
import com.service.scim.utils.MapConverter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import java.util.*;
import static com.service.scim.utils.SCIM.*;

@Service
public class SingleUserService implements ISingleUserService {

    private final IUserRepository userDatabase;
    private final UserMapper userMapper;

    public SingleUserService(IUserRepository userDatabase, UserMapper userMapper) {
        this.userDatabase = userDatabase;
        this.userMapper = userMapper;
    }

    @Override
    public Map singeUserGet(String id, HttpServletResponse response) {
        try {
            //System.out.println(headers);
            User user = userDatabase.findById(id).getFirst();
            return user.toScimResource();

        } catch (Exception e) {
            response.setStatus(404);
            return scimError(USER_NOT_FOUND_MSG, Optional.of(404));
        }
    }

    @Override
    public Map singleUserPut(Map<String, Object> payload, String id) {
        User user = userDatabase.findById(id).getFirst();
        user.update(payload, userMapper);
        userDatabase.save(user);
        return user.toScimResource();
    }

    @Override
    public Map singleUserPatch(Map<String, Object> payload, String id) {
        List schema = (List) payload.get("schemas");
        List<Map> operations = (List) payload.get("Operations");

        //Verify schema
        if (schema == null) {
            return scimError(SCHEMA_ERROR_MSG, Optional.of(400));
        }

        if (operations == null) {
            return scimError(OPERATIONS_ERROR_MSG, Optional.of(400));
        }

        Map<String, Object> userMapOperations = MapConverter.getMapOperations(payload);

        String schemaPatchOp = "urn:ietf:params:scim:api:messages:2.0:PatchOp";
        if (!schema.contains(schemaPatchOp)) {
            return scimError(SCHEMA_NOT_SUPPORTED_MSG, Optional.of(501));
        }

        //Find user for update
        User user = userDatabase.findById(id).getFirst();

        if (user == null) {
            return scimError(String.format(USER_NOT_FOUND, id), Optional.of(404));
        }

        user.update(userMapOperations, userMapper);
        userDatabase.save(user);

        return user.toScimResource();
    }
}
