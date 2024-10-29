package com.service.scim.services;

import java.util.Map;

public interface IGroupsService {
    Map groupsGet(Map<String, String> params);
    Map groupsPost(Map<String, Object> body);
}
