package com.service.scim.services;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.Optional;

public interface ISingleGroupsService {
    Map singeGroupGet( String id, HttpServletResponse response);
    Map singleGroupPut(Map<String, Object> payload, String id);
    Map singleGroupPatch(Map<String, Object> payload, String id);
    Map scimError(String message, Optional<Integer> status_code);
    Map singeGroupDelete(String id, HttpServletResponse response);

}
