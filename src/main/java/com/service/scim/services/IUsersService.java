package com.service.scim.services;
import com.service.scim.models.User;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IUsersService {
    Map usersGet(Map<String, String> params);
    Map usersPost(User newUser);

}
