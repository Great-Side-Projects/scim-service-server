package com.service.scim.controllers;

import com.service.scim.models.Group;
import com.service.scim.services.ISingleGroupsService;
import com.service.scim.utils.PatchRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;
import static com.service.scim.utils.SCIM.scimError;

@RestController
@RequestMapping("/scim/v2/Groups/{id}")
public class SingleGroupController {

    private final ISingleGroupsService singleGroupsService;

    @Autowired
    public SingleGroupController(ISingleGroupsService singleGroupsService) {
        this.singleGroupsService = singleGroupsService;
    }

    /**
     * Queries repositories for {@link Group} with identifier.
     * Updates response with '404' if unable to locate {@link Group}.
     *
     * @param id Group identifier
     * @param params Query parameters from the request
     * @return ResponseEntity with the Group data or 404 if not found
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getGroup(
            @PathVariable String id,
            @RequestParam Map<String, String> params) {

        Map<String, Object> group = singleGroupsService.singeGroupGet(id, params);
        if (group.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(scimError("Group not found",
                            Optional.of(HttpStatus.NOT_FOUND.value())));
        }

        return ResponseEntity.status(HttpStatus.OK).body(group);
    }

    /**
     * Update via Put {@link Group} attributes.
     *
     * @param payload JSON Map of Group attributes
     * @param id Group identifier
     * @return ResponseEntity with the updated Group
     */
    @PutMapping
    public ResponseEntity<Map<String, Object>> updateGroup(
            @RequestBody Map<String, Object> payload,
            @PathVariable String id) {

        Map<String, Object> updatedGroup = singleGroupsService.singleGroupPut(payload, id);
        if(updatedGroup.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(scimError("Group not found",
                            Optional.of(HttpStatus.NOT_FOUND.value())));

        return ResponseEntity.ok(updatedGroup);
    }

    /**
     * Update via Patch {@link Group} attributes.
     *
     * @param payload JSON Map of Group attributes
     * @param id Group identifier
     * @return ResponseEntity with the partially updated Group
     */
    @PatchMapping
    public ResponseEntity<Map<String, Object>> patchGroup(
            @RequestBody Map<String, Object> payload,
            @PathVariable String id) {

        Map<String, Object> mapValidate = PatchRequestValidator.validate(payload);
        if (mapValidate != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapValidate);
        }
        Map<String, Object> updatedGroup = singleGroupsService.singleGroupPatch(payload, id);

        if(updatedGroup.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(scimError("Group not found",
                            Optional.of(HttpStatus.NOT_FOUND.value())));

        return ResponseEntity.ok(updatedGroup);
    }

    /**
     * Delete {@link Group} with identifier.
     *
     * @param id Group identifier
     * @return ResponseEntity with no content (204) on success
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteGroup(
            @PathVariable String id) {

        Map<String, Object> groupDeleted = singleGroupsService.singeGroupDelete(id);

        if (groupDeleted.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.noContent().build();
    }
}
