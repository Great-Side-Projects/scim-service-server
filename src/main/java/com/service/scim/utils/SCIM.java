package com.service.scim.utils;

import com.service.scim.models.User;
import java.util.*;

public class SCIM {

    public static final String USER_NOT_FOUND_MSG = "User not found";
    public static final String SCHEMA_ERROR_MSG = "Payload must contain schema attribute.";
    public static final String OPERATIONS_ERROR_MSG = "Payload must contain operations attribute.";
    public static final String SCHEMA_NOT_SUPPORTED_MSG = "The 'schemas' type in this request is not supported.";
    public static final String USER_NOT_FOUND = "User '%s' was not found.";

    /**
     * Output custom error message with response code
     * @param message Scim error message
     * @param status_code Response status code
     * @return JSON {@link Map} of {@link User}
     */
    public static Map scimError(String message, Optional<Integer> status_code) {
        Map<String, Object> returnValue = new HashMap<>();
        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:api:messages:2.0:Error");
        returnValue.put("schemas", schemas);
        returnValue.put("detail", message);

        // Set default to 500
        returnValue.put("status", status_code.orElse(500));
        return returnValue;
    }
}
