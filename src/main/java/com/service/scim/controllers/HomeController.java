package com.service.scim.controllers;

import com.service.scim.repositories.*;
import com.service.scim.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;

/**
 *  URL route (root)/ to userDisplay home page
 */
@Controller
@RequestMapping("/")
public class HomeController {
    private UserDatabase uDb;
    private GroupDatabase gDb;
    private TransactionDatabase tDb;
    private RequestDatabase rDb;
    private GroupMembershipDatabase gmDb;

    @Autowired
    public HomeController(
            UserDatabase uDb,
            GroupDatabase gDb,
            TransactionDatabase tDb,
            RequestDatabase rDb,
            GroupMembershipDatabase gmDb) {
        this.uDb = uDb;
        this.gDb = gDb;
        this.tDb = tDb;
        this.rDb = rDb;
        this.gmDb = gmDb;
    }

    /**
     * Outputs all active users, groups and transaction logs to web view
     * @param model UI Model
     * @return HTML page to render by name
     */
    @RequestMapping(method = RequestMethod.GET)
    public String home(ModelMap model) {
        List<User> users = uDb.findAll();
        List<Group> groups = gDb.findAll();
        List<Transaction> transactions = tDb.findAll();
        List<Request> requests = rDb.findAll();
        List<GroupMembership> groupMemberships = gmDb.findAll();
        model.addAttribute("users", users);
        model.addAttribute("groups", groups);
        model.addAttribute("transactions", transactions);
        model.addAttribute("requests", requests);
        model.addAttribute("groupMemberships", groupMemberships);
        return "home";
    }
}
