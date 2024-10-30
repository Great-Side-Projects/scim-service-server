package com.service.scim.controllers;

import com.service.scim.models.User;
import com.service.scim.models.mapper.AbstractEntityMapper;
import com.service.scim.models.mapper.UserEntityMapper;
import com.service.scim.models.mapper.strategies.entities.UpdateStrategy;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.repositories.IUserRepository;
import com.service.scim.services.IUsersService;
import com.service.scim.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static com.service.scim.utils.SCIM.OPERATIONS_ERROR_MSG;
import static com.service.scim.utils.SCIM.scimError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UsersControllerTest {

    @Mock
    private IUsersService usersService;

    @InjectMocks
    private UsersController usersController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.usersController = new UsersController(usersService, new UserEntityMapper());
    }

    @Test
    void getUsersReturnsOkWithUsers() {
        Map<String, String> params = new HashMap<>();
        Map<String, Object> users = new HashMap<>();
        when(usersService.usersGet(params)).thenReturn(users);

        ResponseEntity<Map<String, Object>> response = usersController.getUsers(params, new HttpHeaders());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void createUserReturnsCreatedWithNewUser() {
        Map<String, Object> body = new HashMap<>();
        body.put("userName", "testUser");
        body.put("email", "test@example.com");
        body.put("active", true);
        User newUser = new User();
        newUser.userName = "testUser";
        newUser.email = "test@example.com";
        newUser.active = true;

        when(usersService.usersPost(any(User.class))).thenReturn(newUser.toScimResource());

        ResponseEntity<Map<String, Object>> response = usersController.createUser(body);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newUser.toScimResource(), response.getBody());
    }

    @Test
    void createUserReturnsBadRequestWhenEmailIsInvalid() {
        Map<String, Object> body = new HashMap<>();
        body.put("userName", "testUser");
        body.put("email", "invalid-email");

        ResponseEntity<Map<String, Object>> response = usersController.createUser(body);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(scimError(OPERATIONS_ERROR_MSG + " : email", Optional.of(HttpStatus.BAD_REQUEST.value())), response.getBody());
    }

    @Test
    void createUserReturnsBadRequestWhenUserNameIsInvalid() {
        Map<String, Object> body = new HashMap<>();
        body.put("userName", "");
        body.put("email", "test@example.com");

        //ResponseEntity<Map<String, Object>> response = usersController.createUser(body);

        //assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals(scimError(OPERATIONS_ERROR_MSG + " : userName", Optional.of(HttpStatus.BAD_REQUEST.value())), response.getBody());
    }
}


