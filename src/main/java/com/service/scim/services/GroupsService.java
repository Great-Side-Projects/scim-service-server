package com.service.scim.services;

import com.service.scim.models.mapper.AbstractEntityMapper;
import com.service.scim.repositories.IGroupRepository;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import com.service.scim.services.specifications.Specifications;
import com.service.scim.utils.ListResponse;
import com.service.scim.utils.PageRequestBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class GroupsService implements IGroupsService {

    private final IGroupRepository groupRepository;
    private final IGroupMembershipRepository groupMembershipRepository;
    private final AbstractEntityMapper<Group> groupEntityMapper;

    @Autowired
    public GroupsService(IGroupRepository groupRepository, IGroupMembershipRepository groupMembershipRepository, AbstractEntityMapper<Group> groupEntityMapper) {
        this.groupRepository = groupRepository;
        this.groupMembershipRepository = groupMembershipRepository;
        this.groupEntityMapper = groupEntityMapper;
    }

    /**
     * Support pagination and filtering by displayName
     *
     * @param params Payload from HTTP request
     * @return JSON {@link Map} {@link ListResponse}
     */
    @Override
    public Map groupsGet(Map<String, String> params) {
        PageRequest pageRequest = PageRequestBuilder.build(params);
        Page<Group> groups = groupRepository.findAll(Specifications.createSpecification(params), pageRequest);

        List<String> groupsIds = groups.getContent().stream().map(group -> group.id).toList();
        //TODO: excludedAttributes=members
        //if (params.containsKey("excludedAttributes") && params.get("excludedAttributes").equals("members")) {

        Page<GroupMembership> groupMemberships = groupMembershipRepository.findByGroupIds(groupsIds, PageRequestBuilder.build());

        List<Group> foundGroups = groups.getContent();

        // Convert optional values into Optionals for ListResponse Constructor
        ListResponse<Group> returnValue = new ListResponse<>(
                foundGroups,
                Optional.of(pageRequest.getPageNumber()),
                Optional.of(pageRequest.getPageSize()),
                Optional.of((int) groups.getTotalElements()),
                groupMemberships.getContent());

        return returnValue.toScimResource();
    }

    /**
     * Creates new {@link Group} with given attributes
     *
     * @param body JSON {@link Map} of {@link Group} attributes
     * @param response HTTP response
     * @return JSON {@link Map} of {@link Group}
     */
    @Override
    @Transactional
    public Map groupsPost(Map<String, Object> body, HttpServletResponse response) {
        Group newGroup = new Group(body, groupEntityMapper);

        newGroup.id = UUID.randomUUID().toString();
        groupRepository.save(newGroup);

        if (body.containsKey("members")) {
            ArrayList<Map<String, Object>> members = (ArrayList<Map<String, Object>>) body.get("members");

            List<GroupMembership> newMemberships = members.stream()
                    .map(member -> new GroupMembership(member, newGroup.id, newGroup.displayName)).toList();

            groupMembershipRepository.saveAll(newMemberships);
            newGroup.setGroupMemberships(newMemberships);
        }

        response.setStatus(201);
        return newGroup.toScimResource();
    }
}
