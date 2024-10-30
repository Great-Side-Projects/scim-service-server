package com.service.scim.utils;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SCIMTest {

    @Test
    void scimErrorReturnsCorrectJsonStructure() {
        Map<String, Object> result = SCIM.scimError("Error message", Optional.of(400));

        assertNotNull(result);
        assertEquals("Error message", result.get("detail"));
        assertEquals(400, result.get("status"));
        assertTrue(((List<?>) result.get("schemas")).contains("urn:ietf:params:scim:api:messages:2.0:Error"));
    }

    @Test
    void scimErrorHandlesNullStatusCode() {
        Map<String, Object> result = SCIM.scimError("Error message", Optional.empty());

        assertNotNull(result);
        assertEquals("Error message", result.get("detail"));
        assertEquals(500, result.get("status"));
        assertTrue(((List<?>) result.get("schemas")).contains("urn:ietf:params:scim:api:messages:2.0:Error"));
    }

    @Test
    void isValidEmailReturnsTrueForValidEmail() {
        boolean result = SCIM.isValidEmail("test@example.com");

        assertTrue(result);
    }

    @Test
    void isValidEmailReturnsFalseForInvalidEmail() {
        boolean result = SCIM.isValidEmail("invalid-email");

        assertFalse(result);
    }

    @Test
    void isValidEmailReturnsFalseForNullEmail() {
        boolean result = SCIM.isValidEmail(null);

        assertFalse(result);
    }

    @Test
    void isValidUserNameReturnsTrueForValidUserName() {
        boolean result = SCIM.isValidUserName("validUserName");

        assertTrue(result);
    }

    @Test
    void isValidUserNameReturnsFalseForEmptyUserName() {
        boolean result = SCIM.isValidUserName("");

        assertFalse(result);
    }

    @Test
    void isValidUserNameReturnsFalseForNullUserName() {
        boolean result = SCIM.isValidUserName(null);
    }
}
