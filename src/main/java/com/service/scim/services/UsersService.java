package com.service.scim.services;

import com.service.scim.models.mapper.UserEntityMapper;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.repositories.IUserRepository;
import com.service.scim.models.GroupMembership;
import com.service.scim.models.User;
import com.service.scim.services.specifications.FilterSpecifications;
import com.service.scim.utils.ListResponse;
import com.service.scim.utils.PageRequestBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.*;
import static com.service.scim.utils.SCIM.*;

@Service
public class UsersService implements IUsersService {
    private final IUserRepository userRepository;
    private final IGroupMembershipRepository groupMembershipRepository;
    private final UserEntityMapper userEntityMapper;

    public UsersService(IUserRepository userRepository,
                        IGroupMembershipRepository groupMembershipRepository,
                        UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.groupMembershipRepository = groupMembershipRepository;
        this.userEntityMapper = userEntityMapper;
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
    public Map usersPost(Map<String, Object> body, HttpServletResponse response) {
        User newUser = new User(body, userEntityMapper);
        newUser.id = UUID.randomUUID().toString();

        if (newUser.email == null || newUser.email.isEmpty()) {
            response.setStatus(400);
            return scimError(OPERATIONS_ERROR_MSG + " : email", Optional.of(400));
        }

        if (newUser.userName == null || newUser.userName.isEmpty()) {
            response.setStatus(400);
            return scimError(OPERATIONS_ERROR_MSG + " : userName", Optional.of(400));
        }

        userRepository.save(newUser);
        response.setStatus(201);
        return newUser.toScimResource();
    }
}
