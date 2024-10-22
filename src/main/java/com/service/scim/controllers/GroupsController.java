package com.service.scim.controllers;

import com.service.scim.models.Group;
import com.service.scim.services.IGroupsService;
import com.service.scim.utils.ListResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 *  URL route (root)/scim/v2/Groups
 */
@Controller
@RequestMapping("/scim/v2/Groups")
public class GroupsController {

    IGroupsService groupsService;

    @Autowired
    public GroupsController(IGroupsService groupsService) {
        this.groupsService = groupsService;
    }

    /**
     * Support pagination and filtering by displayName
     * @param params Payload from HTTP request
     * @return JSON {@link Map} {@link ListResponse}
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Map groupsGet(
            @RequestParam Map<String, String> params) {
        return groupsService.groupsGet(params);
    }

    /**
     * Creates new {@link Group} with given attributes
     * @param body JSON {@link Map} of {@link Group} attributes
     * @param response HTTP response
     * @return JSON {@link Map} of {@link Group}
     */
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody Map groupsPost(
            @RequestBody Map<String, Object> body,
            HttpServletResponse response){
        return groupsService.groupsPost(body, response);
    }
}
