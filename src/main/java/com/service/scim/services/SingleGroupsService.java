package com.service.scim.services;

import com.service.scim.models.mapper.AbstractEntityMapper;
import com.service.scim.models.mapper.strategies.patchoperation.IPatchOperationStrategy;
import com.service.scim.repositories.IGroupRepository;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import com.service.scim.services.factories.PatchOperationFactory;
import com.service.scim.utils.MapConverter;
import com.service.scim.utils.PageRequestBuilder;
import com.service.scim.utils.PatchRequestValidator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Add this import statement to update

import java.util.*;

import static com.service.scim.utils.SCIM.*;

@Service
public class SingleGroupsService implements ISingleGroupsService {

    private final IGroupRepository groupRepository;
    private final IGroupMembershipRepository groupMembershipRepository;
    private final AbstractEntityMapper<Group> groupEntityMapper;
    private final PatchOperationFactory patchOperationFactory;

    @Autowired
    public SingleGroupsService(IGroupRepository groupRepository,
                               IGroupMembershipRepository groupMembershipRepository,
                               AbstractEntityMapper<Group> groupEntityMapper, PatchOperationFactory patchOperationFactory) {

        this.groupRepository = groupRepository;
        this.groupMembershipRepository = groupMembershipRepository;
        this.groupEntityMapper = groupEntityMapper;
        this.patchOperationFactory = patchOperationFactory;
    }

    /**
     * Queries repositories for {@link Group} with identifier
     * Updates response code with '404' if unable to locate {@link Group}
     *
     * @param id       {@link Group#id}
     * @param response HTTP Response
     * @return {@link #scimError(String, Optional)} / JSON {@link Map} of {@link Group}
     */
    @Override
    public Map singeGroupGet(String id, HttpServletResponse response, Map<String, String> params) {
        try {
            Optional<Group> group = groupRepository.findById(id);
            if (!group.isPresent()) {
                response.setStatus(404);
                return scimError("Group not found", Optional.of(404));
            }
            //TODO: params exludedAttributes=members

            group.get().setGroupMemberships(
                    groupMembershipRepository
                            .findByGroupId(id, PageRequestBuilder.build())
                            .getContent());

            return group.get().toScimResource();
        } catch (Exception e) {
            response.setStatus(404);
            return scimError("Group not found", Optional.of(404));
        }
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
        Optional<Group> ogroup = groupRepository.findById(id);
        if (!ogroup.isPresent()) {
            return scimError("Group not found", Optional.of(404));
        }
        Group group = ogroup.get();

        // Update group display name if changed in members
        if (!group.displayName.equals(payload.get("displayName"))) {
            String newDisplayName = payload.get("displayName").toString();
            groupMembershipRepository.updateGroupDisplayByGroupId(id, newDisplayName);
        }

        // Add new members to group if they do not exist in the group already
        if (payload.containsKey("members")) {
            ArrayList<Map<String, Object>> members = (ArrayList<Map<String, Object>>) payload.get("members");
            List<GroupMembership> newMemberships = members.stream()
                    .filter(member ->
                            !groupMembershipRepository.existsByGroupIdAndUserId(group.id, member.get("value").toString())
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
     * @return {@link #scimError(String, Optional)} / JSON {@link Map} of {@link Group}
     */
    @Override
    @Transactional
    public Map singleGroupPatch(Map<String, Object> payload, String id) {

        Map mapValidate = PatchRequestValidator.validate(payload);
        if (mapValidate != null) {
            return mapValidate;
        }

        Optional<Group> oGroup = groupRepository.findById(id);

        if (!oGroup.isPresent()) {
            return scimError("Group '" + id + "' was not found.", Optional.of(404));
        }
        Group group = oGroup.get();
        Map<String, Object> groupMapOperations = MapConverter.getMapOperations(payload);

        // Update group display name
        group.update(groupMapOperations, groupEntityMapper);

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
     * @param id       {@link Group#id}
     * @param response HTTP Response
     * @return {@link #scimError(String, Optional)} / JSON {@link Map} of {@link Group}
     */
    @Override
    @Transactional
    public Map singeGroupDelete(String id, HttpServletResponse response) {
        try {
            Optional<Group> group = groupRepository.findById(id);
            if (!group.isPresent()) {
                response.setStatus(404);
                return scimError("Group not found", Optional.of(404));
            }
            Page<GroupMembership> toDelete = groupMembershipRepository.findByGroupId(id, PageRequest.of(0, Integer.MAX_VALUE));
            groupMembershipRepository.deleteAll(toDelete);
            groupRepository.delete(group.get());
            response.setStatus(204);
            return group.get().toScimResource();
        } catch (Exception e) {
            response.setStatus(404);
            return scimError("Group not found", Optional.of(404));
        }
    }
}
