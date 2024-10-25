package com.service.scim.services;

import com.service.scim.models.mapper.AbstractEntityMapper;
import com.service.scim.models.mapper.strategies.patchoperation.IPatchOperationStrategy;
import com.service.scim.repositories.IGroupRepository;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import com.service.scim.repositories.IUserRepository;
import com.service.scim.services.factories.PatchOperationFactory;
import com.service.scim.utils.MapConverter;
import com.service.scim.utils.PageRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Add this import statement to update

import java.util.*;

@Service
public class SingleGroupsService implements ISingleGroupsService {

    private final IGroupRepository groupRepository;
    private final IGroupMembershipRepository groupMembershipRepository;
    private final AbstractEntityMapper<Group> groupEntityMapper;
    private final PatchOperationFactory patchOperationFactory;
    private final IUserRepository userRepository;

    @Autowired
    public SingleGroupsService(IGroupRepository groupRepository,
                               IGroupMembershipRepository groupMembershipRepository,
                               AbstractEntityMapper<Group> groupEntityMapper,
                               PatchOperationFactory patchOperationFactory,
                               IUserRepository userRepository
    ) {

        this.groupRepository = groupRepository;
        this.groupMembershipRepository = groupMembershipRepository;
        this.groupEntityMapper = groupEntityMapper;
        this.patchOperationFactory = patchOperationFactory;
        this.userRepository = userRepository;
    }

    /**
     * Queries repositories for {@link Group} with identifier
     * Updates response code with '404' if unable to locate {@link Group}
     *
     * @param id {@link Group#id}
     * @return {@link Map} of {@link Group}
     */
    @Override
    public Map singeGroupGet(String id, Map<String, String> params) {

        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()) {
            return Map.of();
        }

        if (params.containsKey("excludedAttributes") && params.get("excludedAttributes").equals("members")) {
            return group.get().toScimResource();
        }

        group.get().setGroupMemberships(
                groupMembershipRepository
                        .findByGroupId(id, PageRequestBuilder.build())
                        .getContent());

        return group.get().toScimResource();
    }

    /**
     * Update via Put {@link Group} attributes
     *
     * @param payload Payload from HTTP request
     * @param id      {@link Group#id}
     * @return JSON {@link Map} of {@link Group}
     */
    @Override
    @Transactional
    public Map singleGroupPut(Map<String, Object> payload, String id) {
        Optional<Group> oGroup = groupRepository.findById(id);
        if (oGroup.isEmpty()) {
            return Map.of();
        }
        Group group = oGroup.get();

        // Update group display name
        if (!group.displayName.equals(payload.get("displayName"))) {
            String newDisplayName = payload.get("displayName").toString();
            groupMembershipRepository.updateGroupDisplayByGroupId(id, newDisplayName);
        }

        // Add new members to group if they do not exist in the group already
        if (payload.containsKey("members")) {
            ArrayList<Map<String, Object>> members = (ArrayList<Map<String, Object>>) payload.get("members");
            List<GroupMembership> newMemberships = members.stream()
                    .filter(member ->
                            (!groupMembershipRepository.existsByGroupIdAndUserId(group.id, member.get("value").toString()) &&
                                    userRepository.existsById(member.get("value").toString()))
                    )
                    .map(member -> new GroupMembership(member, group.id, group.displayName)).toList();

            if (!newMemberships.isEmpty()) {
                groupMembershipRepository.saveAll(newMemberships);
            }
        }

        group.update(payload, groupEntityMapper);
        groupRepository.save(group);
        return group.toScimResource();
    }

    /**
     * Update via Patch {@link Group} attributes
     *
     * @param payload Payload from HTTP request
     * @param id      {@link Group#id}
     * @return JSON {@link Map} of {@link Group}
     */
    @Override
    @Transactional
    public Map singleGroupPatch(Map<String, Object> payload, String id) {

        Optional<Group> oGroup = groupRepository.findById(id);

        if (oGroup.isEmpty()) {
            return Map.of();
        }
        Group group = oGroup.get();
        Map<String, Object> groupMapOperations = MapConverter.getMapOperations(payload);

        // Update group display name
        group.update(groupMapOperations, groupEntityMapper);

        // Add or delete members from group if they do not exist in the group already
        if (groupMapOperations.containsKey("members")) {
            List<Map<String, Object>> members = (List<Map<String, Object>>) groupMapOperations.get("members");
            String operation = groupMapOperations.get("operation").toString();
            IPatchOperationStrategy strategy = patchOperationFactory.getStrategy(operation);
            strategy.execute(group, members);
        }

        groupRepository.save(group);

        group.setGroupMemberships(groupMembershipRepository
                .findByGroupId(id, PageRequestBuilder.build())
                .getContent());

        return group.toScimResource();
    }

    /**
     * Deletes {@link Group} with identifier
     *
     * @param id {@link Group#id}
     * @return JSON {@link Map} of {@link Group}
     */
    @Override
    @Transactional
    public Map singeGroupDelete(String id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()) {
            return Map.of();
        }

        Page<GroupMembership> toDelete = groupMembershipRepository.findByGroupId(id, PageRequestBuilder.build());
        groupMembershipRepository.deleteAll(toDelete);
        groupRepository.delete(group.get());

        return group.get().toScimResource();
    }
}
