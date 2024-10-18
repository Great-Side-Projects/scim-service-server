package com.service.scim.controllers;

import com.service.scim.models.User;
import com.service.scim.services.ISingleUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 *  URL route (root)/scim/v2/Users/{id}
 */
@Controller
@RequestMapping("/scim/v2/Users/{id}")
@CrossOrigin("*")
public class SingleUserController {
    private final ISingleUserService singleUserService;

    @Autowired
    public SingleUserController(
            ISingleUserService singleUserService)
    {
        this.singleUserService = singleUserService;
    }

    /**
     * Queries repositories for {@link User} with identifier
     * Updates response code with '404' if unable to locate {@link User}
     * @param id {@link User#id}
     * @param response HTTP Response
     * @return  / JSON {@link Map} of {@link User}
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Map singeUserGet(
            @PathVariable String id,
            HttpServletResponse response,
            @RequestHeader(required = false) Map<String, String> headers) {

            //System.out.println(headers);
            return singleUserService.singeUserGet(id, response);
    }

    /**
     * Update via Put {@link User} attributes
     * @param payload Payload from HTTP request
     * @param id {@link User#id}
     * @return JSON {@link Map} of {@link User}
     */
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody Map singleUserPut(
            @RequestBody Map<String, Object> payload,
            @PathVariable String id,
            @RequestHeader(required = false) Map<String, String> headers) {

        //System.out.println(headers);
        return singleUserService.singleUserPut(payload, id);
    }

    /**
     * Update via Patch {@link User} attributes
     * @param payload Payload from HTTP request
     * @param id {@link User#id}
     * @return  / JSON {@link Map} of {@link User}
     */
    @RequestMapping(method = RequestMethod.PATCH)
    public @ResponseBody Map singleUserPatch(
            @RequestBody Map<String, Object> payload,
            @PathVariable String id,
            @RequestHeader(required = false) Map<String, String> headers) {
        //System.out.println(headers);
        return singleUserService.singleUserPatch(payload, id);
    }
}
