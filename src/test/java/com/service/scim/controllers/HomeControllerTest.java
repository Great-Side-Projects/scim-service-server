package com.service.scim.controllers;

import com.service.scim.models.*;
import com.service.scim.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ModelMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class HomeControllerTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IGroupRepository groupRepository;

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private IRequestRepository requestRepository;

    @Mock
    private IGroupMembershipRepository groupMembershipRepository;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void homeReturnsHomeViewWithAllData() {
        List<User> users = Arrays.asList(new User(), new User());
        List<Group> groups = Arrays.asList(new Group(), new Group());
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        List<Request> requests = Arrays.asList(new Request(), new Request());
        List<GroupMembership> groupMemberships = Arrays.asList(new GroupMembership(), new GroupMembership());

        when(userRepository.findAll()).thenReturn(users);
        when(groupRepository.findAll()).thenReturn(groups);
        when(transactionRepository.findAll()).thenReturn(transactions);
        when(requestRepository.findAll()).thenReturn(requests);
        when(groupMembershipRepository.findAll()).thenReturn(groupMemberships);

        ModelMap model = new ModelMap();
        String viewName = homeController.home(model);

        assertEquals("home", viewName);
        assertEquals(users, model.get("users"));
        assertEquals(groups, model.get("groups"));
        assertEquals(transactions, model.get("transactions"));
        assertEquals(requests, model.get("requests"));
        assertEquals(groupMemberships, model.get("groupMemberships"));
    }

    @Test
    void homeReturnsHomeViewWithEmptyData() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(groupRepository.findAll()).thenReturn(Collections.emptyList());
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());
        when(requestRepository.findAll()).thenReturn(Collections.emptyList());
        when(groupMembershipRepository.findAll()).thenReturn(Collections.emptyList());

        ModelMap model = new ModelMap();
        String viewName = homeController.home(model);

        assertEquals("home", viewName);
        assertEquals(Collections.emptyList(), model.get("users"));
        assertEquals(Collections.emptyList(), model.get("groups"));
        assertEquals(Collections.emptyList(), model.get("transactions"));
        assertEquals(Collections.emptyList(), model.get("requests"));
        assertEquals(Collections.emptyList(), model.get("groupMemberships"));
    }
}