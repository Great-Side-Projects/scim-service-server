package com.service.scim.services;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IUsersService {
    Map usersGet(Map<String, String> params);
    Map usersPost(Map<String, Object> params, HttpServletResponse response);

}
