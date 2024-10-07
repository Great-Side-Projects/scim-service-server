package com.service.scim.services;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IGroupsService {
    Map groupsGet(Map<String, String> params);
    Map groupsPost(Map<String, Object> params, HttpServletResponse response);
}
