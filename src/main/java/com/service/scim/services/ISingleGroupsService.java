package com.service.scim.services;



import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

public interface ISingleGroupsService {
    Map singeGroupGet( String id, HttpServletResponse response);
    Map singleGroupPut(Map<String, Object> payload, String id);
    Map singleGroupPatch(Map<String, Object> payload, String id);
    Map scimError(String message, Optional<Integer> status_code);

}
