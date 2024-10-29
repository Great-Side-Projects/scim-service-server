package com.service.scim.services;

import java.util.Map;

public interface ISingleGroupsService {
    Map singeGroupGet( String id, Map<String, String> params);
    Map singleGroupPut(Map<String, Object> payload, String id);
    Map singleGroupPatch(Map<String, Object> payload, String id);
    Map singeGroupDelete(String id);

}
