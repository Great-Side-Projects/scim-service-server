package com.service.scim.services;

import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.repositories.IUserRepository;
import com.service.scim.models.GroupMembership;
import com.service.scim.models.User;
import com.service.scim.services.specifications.FilterSpecifications;
import com.service.scim.utils.ListResponse;
import com.service.scim.utils.PageRequestBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UsersService implements IUsersService {
    private final IUserRepository userRepository;
    private final IGroupMembershipRepository groupMembershipRepository;


    public UsersService(IUserRepository userRepository,
                        IGroupMembershipRepository groupMembershipRepository) {
        this.userRepository = userRepository;
        this.groupMembershipRepository = groupMembershipRepository;
    }

    @Override
    public Map usersGet(Map<String, String> params) {
        PageRequest pageRequest = PageRequestBuilder.build(params);
        Page<User> users = userRepository.findAll(FilterSpecifications.createSpecification(params), pageRequest);

        List<String> userIds = users.getContent().stream().map(user -> user.id).toList();
        Page<GroupMembership> groupMemberships = groupMembershipRepository.findByUserIds(userIds, PageRequestBuilder.build());

        ListResponse<User> returnValue = new ListResponse<>(
                users.getContent(),
                Optional.of(pageRequest.getPageNumber()),
                Optional.of(pageRequest.getPageSize()),
                Optional.of((int) users.getTotalElements()),
                groupMemberships.getContent()
        );

        return returnValue.toScimResource();
    }

    @Override
    public Map usersPost(User newUser) {
        newUser.id = UUID.randomUUID().toString();
        userRepository.save(newUser);
        return newUser.toScimResource();
    }
}
