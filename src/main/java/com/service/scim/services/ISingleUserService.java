package com.service.scim.services;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface ISingleUserService {
    Map singeUserGet(String id, HttpServletResponse response);
    Map singleUserPut(Map<String, Object> payload, String id);
    Map singleUserPatch(Map<String, Object> payload, String id);
}
