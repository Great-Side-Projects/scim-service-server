package com.service.scim.utils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.service.scim.utils.SCIM.scimError;

public class PatchRequestValidator {

    public static Map validate(Map<String, Object> payload) {
        List<String> schema = (List<String>) payload.get("schemas");
        List<Map<String, Object>> operations = (List<Map<String, Object>>) payload.get("Operations");

        if (schema == null) {
            // throw new IllegalArgumentException("Payload must contain schema attribute.");
            return scimError("Payload must contain schema attribute.", Optional.of(400));
        }
        if (operations == null) {
            // throw new IllegalArgumentException("Payload must contain operations attribute.");
            return scimError("Payload must contain operations attribute.", Optional.of(400));
        }

        String schemaPatchOp = "urn:ietf:params:scim:api:messages:2.0:PatchOp";
        if (!schema.contains(schemaPatchOp)) {
            //throw new UnsupportedOperationException(
            //        "The 'schemas' type in this request is not supported..");
            return scimError("The 'schemas' type in this request is not supported.", Optional.of(501));
        }
        return null;
    }
}
