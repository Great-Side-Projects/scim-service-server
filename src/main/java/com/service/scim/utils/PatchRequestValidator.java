package com.service.scim.utils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import static com.service.scim.utils.SCIM.*;

public class PatchRequestValidator {

    public static Map validate(Map<String, Object> payload) {
        List<String> schema = (List<String>) payload.get("schemas");
        List<Map<String, Object>> operations = (List<Map<String, Object>>) payload.get("Operations");

        if (schema == null) {
            return scimError(SCHEMA_ERROR_MSG, Optional.of(400));
        }
        if (operations == null) {
            return scimError(OPERATIONS_ERROR_MSG, Optional.of(400));
        }

        String schemaPatchOp = "urn:ietf:params:scim:api:messages:2.0:PatchOp";
        if (!schema.contains(schemaPatchOp)) {
            return scimError(SCHEMA_NOT_SUPPORTED_MSG, Optional.of(501));
        }
        return null;
    }
}
