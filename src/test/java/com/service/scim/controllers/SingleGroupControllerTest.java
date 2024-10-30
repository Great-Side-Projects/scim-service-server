package com.service.scim.controllers;

import com.service.scim.services.ISingleGroupsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static com.service.scim.utils.SCIM.scimError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SingleGroupControllerTest {

    @Mock
    private ISingleGroupsService singleGroupsService;

    @InjectMocks
    private SingleGroupController singleGroupController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getGroupReturnsOkWithGroup() {
        String groupId = "test-group-id";
        Map<String, String> params = new HashMap<>();
        Map<String, Object> group = new HashMap<>();
        group.put("id", groupId);
        when(singleGroupsService.singeGroupGet(groupId, params)).thenReturn(group);

        ResponseEntity<Map<String, Object>> response = singleGroupController.getGroup(groupId, params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(group, response.getBody());
    }

    @Test
    void getGroupReturnsNotFoundWhenGroupDoesNotExist() {
        String groupId = "non-existent-group-id";
        Map<String, String> params = new HashMap<>();
        when(singleGroupsService.singeGroupGet(groupId, params)).thenReturn(new HashMap<>());

        ResponseEntity<Map<String, Object>> response = singleGroupController.getGroup(groupId, params);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(scimError("Group not found", Optional.of(HttpStatus.NOT_FOUND.value())), response.getBody());
    }

    @Test
    void updateGroupReturnsOkWithUpdatedGroup() {
        String groupId = "test-group-id";
        Map<String, Object> payload = new HashMap<>();
        payload.put("displayName", "Updated Group");
        Map<String, Object> updatedGroup = new HashMap<>();
        updatedGroup.put("id", groupId);
        updatedGroup.put("displayName", "Updated Group");
        when(singleGroupsService.singleGroupPut(payload, groupId)).thenReturn(updatedGroup);

        ResponseEntity<Map<String, Object>> response = singleGroupController.updateGroup(payload, groupId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedGroup, response.getBody());
    }

    @Test
    void updateGroupReturnsNotFoundWhenGroupDoesNotExist() {
        String groupId = "non-existent-group-id";
        Map<String, Object> payload = new HashMap<>();
        when(singleGroupsService.singleGroupPut(payload, groupId)).thenReturn(new HashMap<>());

        ResponseEntity<Map<String, Object>> response = singleGroupController.updateGroup(payload, groupId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(scimError("Group not found", Optional.of(HttpStatus.NOT_FOUND.value())), response.getBody());
    }

    @Test
    void patchGroupReturnsOkWithPartiallyUpdatedGroup() {
        String groupId = "test-group-id";
        Map<String, Object> payload = new HashMap<>();
        payload.put("displayName", "Partially Updated Group");
        payload.put("schemas", List.of("urn:ietf:params:scim:api:messages:2.0:PatchOp"));
        payload.put("Operations", List.of(Map.of("op", "Replace")));
        Map<String, Object> updatedGroup = new HashMap<>();
        updatedGroup.put("id", groupId);
        updatedGroup.put("displayName", "Partially Updated Group");
        when(singleGroupsService.singleGroupPatch(payload, groupId)).thenReturn(updatedGroup);
        ResponseEntity<Map<String, Object>> response = singleGroupController.patchGroup(payload, groupId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedGroup, response.getBody());
    }

    @Test
    void patchGroupReturnsNotFoundWhenGroupDoesNotExist() {
        String groupId = "non-existent-group-id";
        Map<String, Object> payload = new HashMap<>();
        payload.put("schemas", List.of("urn:ietf:params:scim:api:messages:2.0:PatchOp"));
        payload.put("Operations", List.of(Map.of("op", "Replace")));
        when(singleGroupsService.singleGroupPatch(payload, groupId)).thenReturn(new HashMap<>());

        ResponseEntity<Map<String, Object>> response = singleGroupController.patchGroup(payload, groupId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(scimError("Group not found", Optional.of(HttpStatus.NOT_FOUND.value())), response.getBody());
    }

    @Test
    void deleteGroupReturnsNoContentWhenGroupIsDeleted() {
        String groupId = "test-group-id";
        Map<String, Object> groupDeleted = new HashMap<>();
        groupDeleted.put("id", groupId);
        when(singleGroupsService.singeGroupDelete(groupId)).thenReturn(groupDeleted);

        ResponseEntity<Void> response = singleGroupController.deleteGroup(groupId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteGroupReturnsNotFoundWhenGroupDoesNotExist() {
        String groupId = "non-existent-group-id";
        when(singleGroupsService.singeGroupDelete(groupId)).thenReturn(new HashMap<>());

        ResponseEntity<Void> response = singleGroupController.deleteGroup(groupId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}