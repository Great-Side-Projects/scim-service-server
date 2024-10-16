package com.service.scim.services;

import com.service.scim.repositories.GroupDatabase;
import com.service.scim.repositories.GroupMembershipDatabase;
import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import com.service.scim.utils.ListResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GroupsService implements IGroupsService {

    GroupDatabase db;
    GroupMembershipDatabase gmDb;

    @Autowired
    public GroupsService(GroupDatabase db, GroupMembershipDatabase gmDb) {
        this.db = db;
        this.gmDb = gmDb;
    }

    /**
     * Support pagination and filtering by displayName
     * @param params Payload from HTTP request
     * @return JSON {@link Map} {@link ListResponse}
     */
    @Override
    public Map groupsGet(Map<String, String> params) {
        Page<Group> groups;

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
                    case "displayName":
                        groups = db.findByDisplayname(searchValue, pageRequest);
                        break;
                    default:
                        groups = db.findByDisplayname(searchValue, pageRequest);
                        break;
                }
            } else {
                groups = db.findAll(pageRequest);
            }
        } else {
            groups = db.findAll(pageRequest);
        }

        List<Group> foundGroups = groups.getContent();
        int totalResults = foundGroups.size();

        // Convert optional values into Optionals for ListResponse Constructor
        ListResponse<Group> returnValue = new ListResponse<>(foundGroups, Optional.of(startIndex),
                Optional.of(count), Optional.of(totalResults));
        HashMap<String, Object> res = returnValue.toScimResource();
        ArrayList<HashMap<String, Object>> resG = (ArrayList) res.get("Resources");
        ArrayList<HashMap<String, Object>> resGN = new ArrayList<>();

        for (HashMap<String, Object> g: resG) {
            PageRequest pReq = PageRequest.of(0, Integer.MAX_VALUE);
            Page<GroupMembership> pg = gmDb.findByGroupId(g.get("id").toString(), pReq);

            if (!pg.hasContent()) {
                continue;
            }

            List<GroupMembership> gmList = pg.getContent();
            ArrayList<Map<String, Object>> gms = new ArrayList<>();

            for (GroupMembership gm: gmList) {
                gms.add(gm.toScimResource());
            }

            g.put("members", gms);
            resGN.add(g);
        }

        res.remove("Resources");
        res.put("Resources", resGN);

        return res;
    }

    /**
     * Creates new {@link Group} with given attributes
     * @param params JSON {@link Map} of {@link Group} attributes
     * @param response HTTP response
     * @return JSON {@link Map} of {@link Group}
     */
    @Override
    public Map groupsPost(Map<String, Object> params, HttpServletResponse response) {
        Group newGroup = new Group(params);

        newGroup.id = UUID.randomUUID().toString();
        db.save(newGroup);

        HashMap<String, Object> res = newGroup.toScimResource();

        if (params.containsKey("members")) {
            ArrayList<Map<String, Object>> members = (ArrayList<Map<String, Object>>) params.get("members");

            for(Map<String, Object> member: members) {
                GroupMembership membership = new GroupMembership(member);
                membership.id = UUID.randomUUID().toString();
                membership.groupId = newGroup.id;
                membership.groupDisplay = newGroup.displayName;

                gmDb.save(membership);
            }

            res.put("members", members);
        }

        response.setStatus(201);
        return res;
    }
}
