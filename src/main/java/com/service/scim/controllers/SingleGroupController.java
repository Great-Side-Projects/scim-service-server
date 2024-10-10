package com.service.scim.controllers;

import com.service.scim.models.Group;
import com.service.scim.services.ISingleGroupsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 *  URL route (root)/scim/v2/Groups/{id}
 */
@Controller
@RequestMapping("/scim/v2/Groups/{id}")
public class SingleGroupController {

    ISingleGroupsService singleGroupsService;

    @Autowired
    public SingleGroupController(ISingleGroupsService singleGroupsService) {
        this.singleGroupsService = singleGroupsService;
    }

    /**
     * Queries database for {@link Group} with identifier
     * Updates response code with '404' if unable to locate {@link Group}
     * @param id {@link Group#id}
     * @param response HTTP Response
     * @return  / JSON {@link Map} of {@link Group}
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Map singeGroupGet(
            @PathVariable String id,
            HttpServletResponse response) {

        return singleGroupsService.singeGroupGet(id, response);
    }

    /**
     * Update via Put {@link Group} attributes
     * @param payload Payload from HTTP request
     * @param id {@link Group#id}
     * @return JSON {@link Map} of {@link Group}
     */
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody Map singleGroupPut(
            @RequestBody Map<String, Object> payload,
            @PathVariable String id) {
        return singleGroupsService.singleGroupPut(payload, id);
    }

    /**
     * Update via Patch {@link Group} attributes
     * @param payload Payload from HTTP request
     * @param id {@link Group#id}
     * @return  / JSON {@link Map} of {@link Group}
     */
    @RequestMapping(method = RequestMethod.PATCH)
    public @ResponseBody Map singleGroupPatch(
            @RequestBody Map<String, Object> payload,
            @PathVariable String id) {
        return singleGroupsService.singleGroupPatch(payload, id);
    }

    /**
     * Delete {@link Group} with identifier
     * @param id {@link Group#id}
     * @param response HTTP Response
     * @return  / JSON {@link Map} of {@link Group}
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    Map singeGroupDelete(
            @PathVariable String id,
            HttpServletResponse response) {
        return singleGroupsService.singeGroupDelete(id, response);
    }
}
