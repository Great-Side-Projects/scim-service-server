package com.service.scim.controllers;

import com.service.scim.models.Group;
import com.service.scim.services.IGroupsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import static com.service.scim.utils.SCIM.GROUP_ALREADY_EXISTS;
import static com.service.scim.utils.SCIM.scimError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class GroupsControllerTest {

    @Mock
    private IGroupsService groupsService;

    @InjectMocks
    private GroupsController groupsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getGroupsReturnsOkWithGroups() {
        Map<String, String> params = new HashMap<>();
        Map<String, Object> groups = new HashMap<>();
        when(groupsService.groupsGet(params)).thenReturn(groups);

        ResponseEntity<Map<String, Object>> response = groupsController.getGroups(params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groups, response.getBody());
    }

    @Test
    void createGroupReturnsCreatedWithNewGroup() {
        Map<String, Object> body = new HashMap<>();
        body.put("displayName", "Test Group");
        Group newGroup = new Group();
        newGroup.id = UUID.randomUUID().toString();
        newGroup.displayName = "Test Group";

        when(groupsService.groupsPost(body)).thenReturn(newGroup.toScimResource());

        ResponseEntity<Map<String, Object>> response = groupsController.createGroup(body, new HttpHeaders());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newGroup.toScimResource(), response.getBody());
    }

    @Test
    void createGroupReturnsBadRequestWhenGroupAlreadyExists() {
        Map<String, Object> body = new HashMap<>();
        body.put("displayName", "Existing Group");
        when(groupsService.groupsPost(body)).thenReturn(new HashMap<>());

        ResponseEntity<Map<String, Object>> response = groupsController.createGroup(body, new HttpHeaders());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(scimError(String.format(GROUP_ALREADY_EXISTS, body.get("displayName")),
                Optional.of(HttpStatus.BAD_REQUEST.value())), response.getBody());
    }
}