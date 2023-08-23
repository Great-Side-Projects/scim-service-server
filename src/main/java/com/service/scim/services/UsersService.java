package com.service.scim.services;
import com.service.scim.database.GroupMembershipDatabase;
import com.service.scim.database.UserDatabase;
import com.service.scim.models.GroupMembership;
import com.service.scim.models.User;
import com.service.scim.utils.ListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UsersService implements IUsersService {
    private final UserDatabase db;
    private final GroupMembershipDatabase gmDb;

    public UsersService(UserDatabase db, GroupMembershipDatabase gmDb) {
        this.db = db;
        this.gmDb = gmDb;
    }

    @Override
    public Map usersGet(Map<String, String> params) {
        Page<User> users;

        // If not given count, default to 100
        int count = (params.get("count") != null) ? Integer.parseInt(params.get("count")) : 100;

        // If not given startIndex, default to 1
        int startIndex = (params.get("startIndex") != null) ? Integer.parseInt(params.get("startIndex")) : 1;

        if(startIndex < 1){
            startIndex = 1;
        }
        startIndex -=1;

        PageRequest pageRequest = PageRequest.of(startIndex, count);

        String filter = params.get("filter");
        if (filter != null && filter.contains("eq")) {
            String regex = "(\\w+) eq \"([^\"]*)\"";
            Pattern response = Pattern.compile(regex);

            Matcher match = response.matcher(filter);
            Boolean found = match.find();
            if (found) {
                String searchKeyName = match.group(1);
                String searchValue = match.group(2);
                switch (searchKeyName) {
                    case "active":
                        users = db.findByActive(Boolean.valueOf(searchValue), pageRequest);
                        break;
                    case "faimlyName":
                        users = db.findByFamilyName(searchValue, pageRequest);
                        break;
                    case "givenName":
                        users = db.findByGivenName(searchValue, pageRequest);
                        break;
                    default:
                        // Defaults to username lookup
                        users = db.findByUsername(searchValue, pageRequest);
                        break;
                }
            } else {
                users = db.findAll(pageRequest);
            }
        } else {
            users = db.findAll(pageRequest);
        }

        List<User> foundUsers = users.getContent();
        int totalResults = foundUsers.size();

        // Convert optional values into Optionals for ListResponse Constructor
        ListResponse<User> returnValue = new ListResponse<>(foundUsers, Optional.of(startIndex),
                Optional.of(count), Optional.of(totalResults));

        HashMap<String, Object> res = returnValue.toScimResource();
        ArrayList<HashMap<String, Object>> resG  = (ArrayList) res.get("Resources");
        ArrayList<HashMap<String, Object>> resGN = new ArrayList<>();

        for (HashMap<String, Object> u: resG) {
            PageRequest           pReq = PageRequest.of(0, Integer.MAX_VALUE);
            Page<GroupMembership> pg   = gmDb.findByUserId(u.get("id").toString(), pReq);

            if (!pg.hasContent()) {
                continue;
            }

            List<GroupMembership> gmList = pg.getContent();
            ArrayList<Map<String, Object>> gms = new ArrayList<>();

            for (GroupMembership gm: gmList) {
                gms.add(gm.toUserScimResource());
            }

            u.put("groups", gms);
            resGN.add(u);
        }

        res.remove("Resources");
        res.put("Resources", resG);

        return res;
    }

    @Override
    public Map usersPost(Map<String, Object> params, HttpServletResponse response) {
        //realizar validacion de email vacio
        User newUser = new User(params);
        newUser.id = UUID.randomUUID().toString();
        db.save(newUser);
        response.setStatus(201);
        return newUser.toScimResource();
    }
}
