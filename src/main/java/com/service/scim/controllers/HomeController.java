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
    private final IUserRepository userDatabase;
    private final IGroupRepository groupDatabase;
    private final ITransactionRepository transactionDatabase;
    private final IRequestRepository requestDatabase;
    private final IGroupMembershipRepository groupMembershipDatabase;

    @Autowired
    public HomeController(
            IUserRepository userDatabase,
            IGroupRepository groupDatabase,
            ITransactionRepository transactionDatabase,
            IRequestRepository requestDatabase,
            IGroupMembershipRepository groupMembershipDatabase) {
        this.userDatabase = userDatabase;
        this.groupDatabase = groupDatabase;
        this.transactionDatabase = transactionDatabase;
        this.requestDatabase = requestDatabase;
        this.groupMembershipDatabase = groupMembershipDatabase;
    }

    /**
     * Outputs all active users, groups and transaction logs to web view
     * @param model UI Model
     * @return HTML page to render by name
     */
    @RequestMapping(method = RequestMethod.GET)
    public String home(ModelMap model) {
        List<User> users = userDatabase.findAll();
        List<Group> groups = groupDatabase.findAll();
        List<Transaction> transactions = transactionDatabase.findAll();
        List<Request> requests = requestDatabase.findAll();
        List<GroupMembership> groupMemberships = groupMembershipDatabase.findAll();
        model.addAttribute("users", users);
        model.addAttribute("groups", groups);
        model.addAttribute("transactions", transactions);
        model.addAttribute("requests", requests);
        model.addAttribute("groupMemberships", groupMemberships);
        return "home";
    }
}
