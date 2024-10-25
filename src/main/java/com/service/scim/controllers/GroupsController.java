package com.service.scim.controllers;

import com.service.scim.models.Group;
import com.service.scim.services.IGroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static com.service.scim.utils.SCIM.GROUP_ALREADY_EXISTS;
import static com.service.scim.utils.SCIM.scimError;

@RestController
@RequestMapping("/scim/v2/Groups")
public class GroupsController {


    private final IGroupsService groupsService;

    @Autowired
    public GroupsController(IGroupsService groupsService) {
        this.groupsService = groupsService;
    }

    /**
     * Support pagination and filtering by displayName
     *
     * @param params Payload from HTTP request
     * @return ResponseEntity with ListResponse of Groups
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getGroups(
            @RequestParam Map<String, String> params) {

        return ResponseEntity.ok(groupsService.groupsGet(params));
    }

    /**
     * Creates new {@link Group} with given attributes
     *
     * @param body JSON Map of Group attributes
     * @return ResponseEntity with created Group
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createGroup(
            @RequestBody Map<String, Object> body,
            @RequestHeader(required = false) HttpHeaders headers) {

        Map<String, Object> newGroup = groupsService.groupsPost(body);
        if (newGroup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(scimError(String.format(GROUP_ALREADY_EXISTS, body.get("displayName")),
                            Optional.of(HttpStatus.BAD_REQUEST.value())));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
    }
}
