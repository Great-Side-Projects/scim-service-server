package com.service.scim.services;

import com.service.scim.models.UserMapper;
import com.service.scim.repositories.GroupMembershipDatabase;
import com.service.scim.repositories.UserDatabase;
import com.service.scim.models.GroupMembership;
import com.service.scim.models.User;
import com.service.scim.utils.ListResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.service.scim.utils.SCIM.*;


@Service
public class UsersService implements IUsersService {
    private final UserDatabase db;
    private final GroupMembershipDatabase gmDb;
    private final UserMapper userMapper;

    public UsersService(UserDatabase db, GroupMembershipDatabase gmDb, UserMapper userMapper) {
        this.db = db;
        this.gmDb = gmDb;
        this.userMapper = userMapper;
    }

    @Override
    public Map usersGet(Map<String, String> params) {
        PageRequest pageRequest = constructPageRequest(params);
        Page<User> users = getUsersBasedOnFilter(params, pageRequest);

        ListResponse<User> returnValue = new ListResponse<>(
                users.getContent(),
                Optional.of(pageRequest.getPageNumber()),
                Optional.of(pageRequest.getPageSize()),
                Optional.of((int)users.getTotalElements())
        );

        return convertUsersToScimResources(returnValue);
    }

    @Override
    public Map usersPost(Map<String, Object> params, HttpServletResponse response) {
        User newUser = createUser(params);

        if (newUser.email == null || newUser.email.isEmpty()) {
            response.setStatus(400);
            return scimError(OPERATIONS_ERROR_MSG + " : email" , Optional.of(400));
        }

        if (newUser.userName == null || newUser.userName.isEmpty()) {
            response.setStatus(400);
            return scimError(OPERATIONS_ERROR_MSG + " : userName" , Optional.of(400));
        }

        db.save(newUser);
        response.setStatus(201);
        return newUser.toScimResource();
    }

    private PageRequest constructPageRequest(Map<String, String> params) {
        int count = Integer.parseInt(params.getOrDefault("count", "100"));
        int startIndex = Integer.parseInt(params.getOrDefault("startIndex", "1"));
        startIndex = (startIndex < 1) ? 1 : startIndex - 1;

        return PageRequest.of(startIndex, count);
    }

    private Page<User> getUsersBasedOnFilter(Map<String, String> params, PageRequest pageRequest) {
        String filter = params.get("filter");
        if (filter != null && filter.contains("eq")) {
            String[] keyValue = extractKeyAndValueFromFilter(filter);
            if (keyValue != null) {
                return findUsersByKeyAndValue(keyValue[0], keyValue[1], pageRequest);
            }
        }
        return db.findAll(pageRequest);
    }

    private String[] extractKeyAndValueFromFilter(String filter) {
        String regex = "(\\w+) eq \"([^\"]*)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(filter);

        if (match.find()) {
            return new String[]{match.group(1), match.group(2)};
        }
        return null;
    }

    private Page<User> findUsersByKeyAndValue(String key, String value, PageRequest pageRequest) {
        switch (key) {
            case "active":
                return db.findByActive(Boolean.valueOf(value), pageRequest);
            case "faimlyName":
                return db.findByFamilyName(value, pageRequest);
            case "givenName":
                return db.findByGivenName(value, pageRequest);
            default:
                return db.findByUsername(value, pageRequest);
        }
    }

    private Map convertUsersToScimResources(ListResponse<User> usersList) {
        Map<String, Object> resources = usersList.toScimResource();
        ArrayList<HashMap<String, Object>> users = (ArrayList) resources.get("Resources");

        for (HashMap<String, Object> user : users) {
            List<GroupMembership> groupMemberships = findGroupMembershipsForUser(user.get("id").toString());
            if (!groupMemberships.isEmpty()) {
                ArrayList<Map<String, Object>> groups = new ArrayList<>();
                for (GroupMembership gm : groupMemberships) {
                    groups.add(gm.toUserScimResource());
                }
                user.put("groups", groups);
            }
        }

        resources.put("Resources", users);
        return resources;
    }

    private List<GroupMembership> findGroupMembershipsForUser(String userId) {
        PageRequest allItemsRequest = PageRequest.of(0, Integer.MAX_VALUE);
        Page<GroupMembership> page = gmDb.findByUserId(userId, allItemsRequest);
        return page.getContent();
    }

    private User createUser(Map<String, Object> params) {
        User newUser = new User(params, userMapper);
        newUser.id = UUID.randomUUID().toString();
        return newUser;
    }
}
