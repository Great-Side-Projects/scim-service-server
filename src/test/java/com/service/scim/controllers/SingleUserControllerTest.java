package com.service.scim.controllers;

import com.service.scim.services.ISingleUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static com.service.scim.utils.SCIM.USER_NOT_FOUND_MSG;
import static com.service.scim.utils.SCIM.scimError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SingleUserControllerTest {


    @Mock
    private ISingleUserService singleUserService;

    @InjectMocks
    private SingleUserController singleUserController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserByIdReturnsOkWithUser() {
        String userId = "test-user-id";
        Map<String, Object> user = new HashMap<>();
        user.put("id", userId);
        when(singleUserService.singeUserGet(userId)).thenReturn(user);

        ResponseEntity<Map<String, Object>> response = singleUserController.getUserById(userId, new HttpHeaders());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUserByIdReturnsNotFoundWhenUserDoesNotExist() {
        String userId = "non-existent-user-id";
        when(singleUserService.singeUserGet(userId)).thenReturn(new HashMap<>());

        ResponseEntity<Map<String, Object>> response = singleUserController.getUserById(userId, new HttpHeaders());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(scimError(USER_NOT_FOUND_MSG, Optional.of(HttpStatus.NOT_FOUND.value())), response.getBody());
    }

    @Test
    void updateUserViaPutReturnsOkWithUpdatedUser() {
        String userId = "test-user-id";
        Map<String, Object> payload = new HashMap<>();
        payload.put("displayName", "Updated User");
        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("id", userId);
        updatedUser.put("displayName", "Updated User");
        when(singleUserService.singleUserPut(payload, userId)).thenReturn(updatedUser);

        ResponseEntity<Map<String, Object>> response = singleUserController.updateUserViaPut(payload, userId, new HttpHeaders());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void updateUserViaPutReturnsNotFoundWhenUserDoesNotExist() {
        String userId = "non-existent-user-id";
        Map<String, Object> payload = new HashMap<>();
        when(singleUserService.singleUserPut(payload, userId)).thenReturn(new HashMap<>());

        ResponseEntity<Map<String, Object>> response = singleUserController.updateUserViaPut(payload, userId, new HttpHeaders());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(scimError(USER_NOT_FOUND_MSG, Optional.of(HttpStatus.NOT_FOUND.value())), response.getBody());
    }

    @Test
    void updateUserViaPatchReturnsOkWithPartiallyUpdatedUser() {
        String userId = "test-user-id";
        Map<String, Object> payload = new HashMap<>();
        payload.put("schemas", List.of("urn:ietf:params:scim:api:messages:2.0:PatchOp"));
        payload.put("Operations", List.of(Map.of("op", "Replace")));
        payload.put("displayName", "Partially Updated User");
        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("id", userId);
        updatedUser.put("displayName", "Partially Updated User");
        when(singleUserService.singleUserPatch(payload, userId)).thenReturn(updatedUser);

        ResponseEntity<Map<String, Object>> response = singleUserController.updateUserViaPatch(payload, userId, new HttpHeaders());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void updateUserViaPatchReturnsNotFoundWhenUserDoesNotExist() {
        String userId = "non-existent-user-id";
        Map<String, Object> payload = new HashMap<>();
        payload.put("schemas", List.of("urn:ietf:params:scim:api:messages:2.0:PatchOp"));
        payload.put("Operations", List.of(Map.of("op", "Replace")));

        when(singleUserService.singleUserPatch(payload, userId)).thenReturn(new HashMap<>());

        ResponseEntity<Map<String, Object>> response = singleUserController.updateUserViaPatch(payload, userId, new HttpHeaders());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(scimError(USER_NOT_FOUND_MSG, Optional.of(HttpStatus.NOT_FOUND.value())), response.getBody());
    }

    @Test
    void deleteUserReturnsNoContentWhenUserIsDeleted() {
        String userId = "test-user-id";
        doNothing().when(singleUserService).singleUserDelete(userId);

        ResponseEntity<Void> response = singleUserController.deleteUser(userId, new HttpHeaders());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteUserReturnsNotFoundWhenUserDoesNotExist() {
        String userId = "non-existent-user-id";

        ResponseEntity<Void> response = singleUserController.deleteUser(userId, new HttpHeaders());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}