package com.service.scim.services;

import com.service.scim.repositories.IGroupDatabase;
import com.service.scim.repositories.IGroupMembershipDatabase;
import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class SingleGroupsService implements ISingleGroupsService {

    IGroupDatabase groupDatabase;
    IGroupMembershipDatabase groupMembershipDatabase;

    @Autowired
    public SingleGroupsService(IGroupDatabase groupDatabase, IGroupMembershipDatabase groupMembershipDatabase) {
        this.groupDatabase = groupDatabase;
        this.groupMembershipDatabase = groupMembershipDatabase;
    }

    /**
     * Queries repositories for {@link Group} with identifier
     * Updates response code with '404' if unable to locate {@link Group}
     * @param id {@link Group#id}
     * @param response HTTP Response
     * @return {@link #scimError(String, Optional)} / JSON {@link Map} of {@link Group}
     */
    @Override
    public Map singeGroupGet(String id, HttpServletResponse response) {
        try {
            Group group = groupDatabase.findById(id).getFirst();
            if (group == null) {
                response.setStatus(404);
                return scimError("Group not found", Optional.of(404));
            }

            HashMap res = group.toScimResource();

            PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
            Page<GroupMembership> gmPage = groupMembershipDatabase.findByGroupId(id, pageRequest);
            List<GroupMembership> gmList = gmPage.getContent();
            ArrayList<Map<String, Object>> gmAL = new ArrayList<>();

            for (GroupMembership gm : gmList) {
                gmAL.add(gm.toScimResource());
            }

            res.put("members", gmAL);
            return res;
        } catch (Exception e) {
            response.setStatus(404);
            return scimError("Group not found", Optional.of(404));
        }
    }

    /**
     * Update via Put {@link Group} attributes
     * @param payload Payload from HTTP request
     * @param id {@link Group#id}
     * @return JSON {@link Map} of {@link Group}
     */
    @Override
    public Map singleGroupPut(Map<String, Object> payload, String id) {
        Group group = groupDatabase.findById(id).getFirst();
        if (group == null) {
            return scimError("Group not found", Optional.of(404));
        }

        if (payload.containsKey("members")) {
            ArrayList<Map<String, Object>> members = (ArrayList<Map<String, Object>>) payload.get("members");

            for (Map<String, Object> member : members) {
                GroupMembership membership = new GroupMembership(member);
                if (!groupMembershipDatabase.existsByUserId(membership.userId))
                {
                    membership.id = UUID.randomUUID().toString();
                    membership.groupId = group.id;
                    membership.groupDisplay = group.displayName;
                    groupMembershipDatabase.save(membership);
                }
            }
        }

        group.update(payload);
        return group.toScimResource();
    }

    /**
     * Update via Patch {@link Group} attributes
     * @param payload Payload from HTTP request
     * @param id {@link Group#id}
     * @return {@link #scimError(String, Optional)} / JSON {@link Map} of {@link Group}
     */
    @Override
    public Map singleGroupPatch(Map<String, Object> payload, String id) {
        List schema = (List) payload.get("schemas");
        List<Map> operations = (List) payload.get("Operations");

        if (schema == null) {
            return scimError("Payload must contain schema attribute.", Optional.of(400));
        }
        if (operations == null) {
            return scimError("Payload must contain operations attribute.", Optional.of(400));
        }

        //Verify schema
        String schemaPatchOp = "urn:ietf:params:scim:api:messages:2.0:PatchOp";
        if (!schema.contains(schemaPatchOp)) {
            return scimError("The 'schemas' type in this request is not supported.", Optional.of(501));
        }

        int found = groupDatabase.findById(id).size();

        if (found == 0) {
            return scimError("Group '" + id + "' was not found.", Optional.of(404));
        }

        //Find user for update
        Group group = groupDatabase.findById(id).get(0);

        HashMap res = group.toScimResource();

        for (Map map : operations) {
            if (map.get("op") == null) {
                continue;
            }

            if (map.get("op").equals("replace")) {
                Map<String, Object> value = (Map) map.get("userId");

                // Use Java reflection to find and set User attribute
                if (value != null) {
                    for (Map.Entry key : value.entrySet()) {
                        try {
                            Field field = group.getClass().getDeclaredField(key.getKey().toString());
                            field.set(group, key.getValue());
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            // Error - Do not update field
                        }
                    }

                    groupDatabase.save(group);
                }
            } else if (map.get("op").equals("add")) {
                if (!map.get("path").equals("members")) {
                    continue;
                }

                ArrayList<Map<String, Object>> value = (ArrayList) map.get("userId");

                if (value != null && !value.isEmpty()) {
                    for (Map val : value) {
                        PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
                        Page<GroupMembership> gmPage = groupMembershipDatabase.findByGroupIdAndUserId(id, val.get("userId").toString(), pageRequest);

                        if (gmPage.hasContent()) {
                            continue;
                        }

                        GroupMembership gm = new GroupMembership(val);
                        gm.id = UUID.randomUUID().toString();
                        gm.groupId = id;
                        groupMembershipDatabase.save(gm);
                    }
                }
            }
        }

        PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
        Page<GroupMembership> gms = groupMembershipDatabase.findByGroupId(id, pageRequest);
        List<GroupMembership> gmList = gms.getContent();
        ArrayList<Map<String, Object>> gmAL = new ArrayList<>();

        for (GroupMembership gm: gmList) {
            gmAL.add(gm.toScimResource());
        }

        res.put("members", gmAL);

        return res;
    }

    /**
     * Output custom error message with response code
     * @param message Scim error message
     * @param status_code Response status code
     * @return JSON {@link Map} of {@link Group}
     */
    @Override
    public Map scimError(String message, Optional<Integer> status_code) {
        Map<String, Object> returnValue = new HashMap<>();
        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:api:messages:2.0:Error");
        returnValue.put("schemas", schemas);
        returnValue.put("detail", message);

        // Set default to 500
        returnValue.put("status", status_code.orElse(500));
        return returnValue;
    }

    /**
     * Deletes {@link Group} with identifier
     * @param id {@link Group#id}
     * @param response HTTP Response
     * @return {@link #scimError(String, Optional)} / JSON {@link Map} of {@link Group}
     */
    @Override
    @Transactional
    public Map singeGroupDelete(String id, HttpServletResponse response) {
        try {
            Group group = groupDatabase.findById(id).getFirst();
            if (group == null) {
                response.setStatus(404);
                return scimError("Group not found", Optional.of(404));
            }
            Page<GroupMembership> toDelete = groupMembershipDatabase.findByGroupId(id, PageRequest.of(0, Integer.MAX_VALUE));
            groupMembershipDatabase.deleteAll(toDelete);
            groupDatabase.delete(group);
            return group.toScimResource();
        } catch (Exception e) {
            response.setStatus(404);
            return scimError("Group not found", Optional.of(404));
        }
    }
}
