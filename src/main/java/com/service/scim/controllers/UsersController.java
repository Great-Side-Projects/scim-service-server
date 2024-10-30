package com.service.scim.controllers;

import com.service.scim.models.User;
import com.service.scim.models.mapper.AbstractEntityMapper;
import com.service.scim.models.mapper.UserEntityMapper;
import com.service.scim.services.IUsersService;
import com.service.scim.utils.SCIM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;
import static com.service.scim.utils.SCIM.OPERATIONS_ERROR_MSG;
import static com.service.scim.utils.SCIM.scimError;

@RestController
@RequestMapping("/scim/v2/Users")
public class UsersController {

    private final IUsersService usersService;
    private final AbstractEntityMapper<User> userEntityMapper;

    @Autowired
    public UsersController(IUsersService usersService, AbstractEntityMapper<User> userEntityMapper) {
        this.usersService = usersService;
        this.userEntityMapper = userEntityMapper;
    }

    /**
     * Supports pagination and filtering by username.
     *
     * @param params Query parameters for pagination and filtering
     * @return ResponseEntity with the list of users and pagination info
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam Map<String, String> params,
            @RequestHeader(required = false) HttpHeaders headers) {

        return ResponseEntity.ok(usersService.usersGet(params));
    }

    /**
     * Creates a new {@link User} with the given attributes.
     *
     * @param body JSON Map containing User attributes
     * @return ResponseEntity with the created User and HTTP status
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(
            @RequestBody Map<String, Object> body) {

        User newUser = new User(body, userEntityMapper);

        if (!SCIM.isValidEmail(newUser.email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(scimError(OPERATIONS_ERROR_MSG + " : email",
                            Optional.of(HttpStatus.BAD_REQUEST.value())
                    ));

        }

        if (!SCIM.isValidUserName(newUser.userName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(scimError(OPERATIONS_ERROR_MSG + " : userName",
                            Optional.of(HttpStatus.BAD_REQUEST.value())
                    ));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.usersPost(newUser));
    }
}
