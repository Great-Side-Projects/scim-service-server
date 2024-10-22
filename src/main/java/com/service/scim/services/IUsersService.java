package com.service.scim.services;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IUsersService {
    Map usersGet(Map<String, String> params);
    Map usersPost(Map<String, Object> body, HttpServletResponse response);

}
