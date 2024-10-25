package com.service.scim.services;
import com.service.scim.models.User;
import java.util.Map;

public interface IUsersService {
    Map usersGet(Map<String, String> params);
    Map usersPost(User newUser);

}
