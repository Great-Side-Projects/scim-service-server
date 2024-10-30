package com.service.scim.utils;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PatchRequestValidatorTest {

    @Test
    void validatePatchRequestWithValidOperations() {
        Map<String, Object> payload = Map.of(
                "schemas", List.of("urn:ietf:params:scim:api:messages:2.0:PatchOp"),
                "Operations", List.of(Map.of("op", "Replace", "path", "active", "value", "true"))
        );

        Map result = PatchRequestValidator.validate(payload);

        assertNull(result);
    }

    @Test
    void validatePatchRequestWithInvalidOperationType() {
        Map<String, Object> payload = Map.of(
                "schemas", List.of(""),
                "Operations", List.of(Map.of("op", "invalidOp", "path", "active", "value", "true"))
        );
        Map result = PatchRequestValidator.validate(payload);

        assertNotNull(result);
    }

    @Test
    void validatePatchRequestWithEmptyOperations() {
        Map<String, Object> payload = Map.of(
                "schemas", List.of("urn:ietf:params:scim:api:messages:2.0:PatchOp"),
                "Operations", List.of()
        );

        Map result = PatchRequestValidator.validate(payload);

        assertNotNull(result);
    }
}
