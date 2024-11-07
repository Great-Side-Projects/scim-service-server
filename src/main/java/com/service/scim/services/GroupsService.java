package com.service.scim.services;

import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import com.service.scim.models.mapper.AbstractEntityMapper;
import com.service.scim.models.mapper.strategies.groupmembership.GroupMembershipAssignerStrategy;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.repositories.IGroupRepository;
import com.service.scim.repositories.IUserRepository;
import com.service.scim.services.specifications.FilterSpecifications;
import com.service.scim.utils.ListResponse;
import com.service.scim.utils.PageRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GroupsService implements IGroupsService {

    private final IGroupRepository groupRepository;
    private final IGroupMembershipRepository groupMembershipRepository;
    private final AbstractEntityMapper<Group> groupEntityMapper;
    private final IUserRepository userRepository;

    @Autowired
    public GroupsService(IGroupRepository groupRepository,
                         IGroupMembershipRepository groupMembershipRepository,
                         AbstractEntityMapper<Group> groupEntityMapper,
                         IUserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupMembershipRepository = groupMembershipRepository;
        this.groupEntityMapper = groupEntityMapper;
        this.userRepository = userRepository;
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
        Page<Group> groups = groupRepository.findAll(FilterSpecifications.createSpecification(params), pageRequest);
        List<Group> foundGroups = groups.getContent();

        Page<GroupMembership> groupMemberships = Page.empty();
        if (!(params.containsKey("excludedAttributes") && params.get("excludedAttributes").equals("members"))) {
            List<String> groupsIds = groups.getContent().stream().map(group -> group.id).toList();
            groupMemberships = groupMembershipRepository.findByGroupIds(groupsIds, PageRequestBuilder.build());
        }

        GroupMembershipAssignerStrategy groupMembershipAssigner = new GroupMembershipAssignerStrategy(groupMemberships.getContent());

        ListResponse<Group> returnValue = new ListResponse.Builder<Group>()
                .withList(foundGroups)
                .withStartIndex(pageRequest.getPageNumber())
                .withCount(pageRequest.getPageSize())
                .withTotalResults((int) groups.getTotalElements())
                .withMembershipAssigner(groupMembershipAssigner)
                .build();

        return returnValue.toScimResource();
    }

    /**
     * Creates new {@link Group} with given attributes
     *
     * @param body JSON {@link Map} of {@link Group} attributes
     * @return JSON {@link Map} of {@link Group}
     */
    @Override
    @Transactional
    public Map groupsPost(Map<String, Object> body) {
        Group newGroup = new Group(body, groupEntityMapper);

        // Check if group already exists
        if (groupRepository.existsByDisplayName(newGroup.displayName)) {
            return Map.of();
        }

        newGroup.id = UUID.randomUUID().toString();
        groupRepository.save(newGroup);

        if (body.containsKey("members")) {
            ArrayList<Map<String, Object>> members = (ArrayList<Map<String, Object>>) body.get("members");

            List<GroupMembership> newMemberships = members.stream()
                    .filter(member ->
                            (!groupMembershipRepository.existsByGroupIdAndUserId(newGroup.id, member.get("value").toString()) &&
                                    userRepository.existsById(member.get("value").toString()))
                    )
                    .map(member -> new GroupMembership(member, newGroup.id, newGroup.displayName)).toList();

            groupMembershipRepository.saveAll(newMemberships);
            newGroup.setGroupMemberships(newMemberships);
        }

        return newGroup.toScimResource();
    }
}
