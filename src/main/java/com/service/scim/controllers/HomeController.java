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
    private final IUserRepository userRepository;
    private final IGroupRepository groupRepository;
    private final ITransactionRepository transactionRepository;
    private final IRequestRepository requestRepository;
    private final IGroupMembershipRepository groupMembershipRepository;

    @Autowired
    public HomeController(
            IUserRepository userRepository,
            IGroupRepository groupRepository,
            ITransactionRepository transactionRepository,
            IRequestRepository requestRepository,
            IGroupMembershipRepository groupMembershipRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.transactionRepository = transactionRepository;
        this.requestRepository = requestRepository;
        this.groupMembershipRepository = groupMembershipRepository;
    }

    /**
     * Outputs all active users, groups and transaction logs to web view
     * @param model UI Model
     * @return HTML page to render by name
     */
    @RequestMapping(method = RequestMethod.GET)
    public String home(ModelMap model) {
        List<User> users = userRepository.findAll();
        List<Group> groups = groupRepository.findAll();
        List<Transaction> transactions = transactionRepository.findAll();
        List<Request> requests = requestRepository.findAll();
        List<GroupMembership> groupMemberships = groupMembershipRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("groups", groups);
        model.addAttribute("transactions", transactions);
        model.addAttribute("requests", requests);
        model.addAttribute("groupMemberships", groupMemberships);
        return "home";
    }
}
