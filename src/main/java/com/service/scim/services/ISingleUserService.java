package com.service.scim.services;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public interface ISingleUserService {
    Map singeUserGet(String id);
    Map singleUserPut(Map<String, Object> payload, String id);
    Map singleUserPatch(Map<String, Object> payload, String id);
    void singleUserDelete(String id);
}
