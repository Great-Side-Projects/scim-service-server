package com.service.scim.services;

import com.service.scim.models.mapper.UserEntityMapper;
import com.service.scim.repositories.IUserRepository;
import com.service.scim.models.User;
import com.service.scim.utils.MapConverter;
import com.service.scim.utils.PatchRequestValidator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import java.util.*;
import static com.service.scim.utils.SCIM.*;

@Service
public class SingleUserService implements ISingleUserService {

    private final IUserRepository userDatabase;
    private final UserEntityMapper userEntityMapper;

    public SingleUserService(IUserRepository userDatabase, UserEntityMapper userEntityMapper) {
        this.userDatabase = userDatabase;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public Map singeUserGet(String id, HttpServletResponse response) {
        try {
            //System.out.println(headers);
            User user = userDatabase.findById(id).get();
            return user.toScimResource();

        } catch (Exception e) {
            response.setStatus(404);
            return scimError(USER_NOT_FOUND_MSG, Optional.of(404));
        }
    }

    @Override
    public Map singleUserPut(Map<String, Object> payload, String id) {
        User user = userDatabase.findById(id).get();
        user.update(payload, userEntityMapper);
        userDatabase.save(user);
        return user.toScimResource();
    }

    @Override
    public Map singleUserPatch(Map<String, Object> payload, String id) {
        List schema = (List) payload.get("schemas");
        List<Map> operations = (List) payload.get("Operations");

        Map validationResult = PatchRequestValidator.validate(payload);
        if (validationResult != null) {
            return validationResult;
        }

        //Find user for update
        Optional<User> user = userDatabase.findById(id);

        if (user.isPresent()) {
            return scimError(String.format(USER_NOT_FOUND, id), Optional.of(404));
        }

        Map<String, Object> userMapOperations = MapConverter.getMapOperations(payload);
        user.get().update(userMapOperations, userEntityMapper);
        userDatabase.save(user.get());

        return user.get().toScimResource();
    }
}
