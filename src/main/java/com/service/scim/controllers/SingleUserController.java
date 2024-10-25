package com.service.scim.controllers;

import com.service.scim.models.User;
import com.service.scim.services.ISingleUserService;
import com.service.scim.utils.PatchRequestValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;
import static com.service.scim.utils.SCIM.*;

@RestController
@RequestMapping("/scim/v2/Users/{id}")
@CrossOrigin("*")
public class SingleUserController {

    private final ISingleUserService singleUserService;

    public SingleUserController(ISingleUserService singleUserService) {
        this.singleUserService = singleUserService;
    }

    /**
     * Get User by ID
     *
     * @param id      {@link User#id}
     * @param headers HTTP headers
     * @return ResponseEntity with User data or 404 status
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getUserById(
            @PathVariable String id,
            @RequestHeader(required = false) HttpHeaders headers) {

        Map<String, Object> user = singleUserService.singeUserGet(id);
        return user.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(scimError(USER_NOT_FOUND_MSG, Optional.of(HttpStatus.NOT_FOUND.value())))
                : ResponseEntity.ok(user);
    }

    /**
     * Update User attributes via PUT
     *
     * @param payload Payload from HTTP request
     * @param id      {@link User#id}
     * @return ResponseEntity with updated User data
     */
    @PutMapping
    public ResponseEntity<Map<String, Object>> updateUserViaPut(
            @RequestBody Map<String, Object> payload,
            @PathVariable String id,
            @RequestHeader(required = false) HttpHeaders headers) {

        Map<String, Object> updatedUser = singleUserService.singleUserPut(payload, id);

        return updatedUser.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(scimError(USER_NOT_FOUND_MSG,
                                Optional.of(HttpStatus.NOT_FOUND.value())
                        ))
                : ResponseEntity.ok(updatedUser);
    }

    /**
     * Update User attributes via PATCH
     *
     * @param payload Payload from HTTP request
     * @param id      {@link User#id}
     * @return ResponseEntity with updated User data
     */
    @PatchMapping
    public ResponseEntity<Map<String, Object>> updateUserViaPatch(
            @RequestBody Map<String, Object> payload,
            @PathVariable String id,
            @RequestHeader(required = false) HttpHeaders headers) {

        Map validationResult = PatchRequestValidator.validate(payload);
        if (validationResult != null) {
            return ResponseEntity.status((int) validationResult.get("status")).body(validationResult);
        }

        Map<String, Object> updatedUser = singleUserService.singleUserPatch(payload, id);
        return updatedUser.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(scimError(USER_NOT_FOUND_MSG, Optional.of(HttpStatus.NOT_FOUND.value())))
                : ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete User by ID
     *
     * @param id {@link User#id}
     * @return ResponseEntity with no content status
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @PathVariable String id,
            @RequestHeader(required = false) HttpHeaders headers) {

        singleUserService.singleUserDelete(id);
        return ResponseEntity.noContent().build();
    }
}
