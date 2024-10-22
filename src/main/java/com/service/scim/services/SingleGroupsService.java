package com.service.scim.services;

import com.service.scim.models.mapper.AbstractEntityMapper;
import com.service.scim.repositories.IGroupRepository;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import com.service.scim.repositories.IUserRepository;
import com.service.scim.utils.MapConverter;
import com.service.scim.utils.PageRequestBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Add this import statement to update

import java.lang.reflect.Field;
import java.util.*;

import static com.service.scim.utils.SCIM.*;

@Service
public class SingleGroupsService implements ISingleGroupsService {

    private final IGroupRepository groupRepository;
    private final IGroupMembershipRepository groupMembershipRepository;
    private final IUserRepository userRepository;
    private final AbstractEntityMapper<Group> groupEntityMapper;

    @Autowired
    public SingleGroupsService(IGroupRepository groupRepository,
                               IGroupMembershipRepository groupMembershipRepository,
                               IUserRepository userRepository, AbstractEntityMapper<Group> groupEntityMapper) {

        this.groupRepository = groupRepository;
        this.groupMembershipRepository = groupMembershipRepository;
        this.userRepository = userRepository;
        this.groupEntityMapper = groupEntityMapper;
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
            Group group = groupRepository.findById(id).getFirst();
            if (group == null) {
                response.setStatus(404);
                return scimError("Group not found", Optional.of(404));
            }
            //TODO: params exludedAttributes=members

            group.setGroupMemberships(
                    groupMembershipRepository
                            .findByGroupId(id, PageRequestBuilder.build())
                            .getContent());

            return group.toScimResource();
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
        Group group = groupRepository.findById(id).getFirst();
        if (group == null) {
            return scimError("Group not found", Optional.of(404));
        }

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
        List schema = (List) payload.get("schemas");
        List<Map> operations = (List) payload.get("Operations");

        if (schema == null) {
            return scimError("Payload must contain schema attribute.", Optional.of(400));
        }
        if (operations == null) {
            return scimError("Payload must contain operations attribute.", Optional.of(400));
        }

        //Verify schema
        String schemaPatchOp = "urn:ietf:params:scim:api:messages:2.0:PatchOp";
        if (!schema.contains(schemaPatchOp)) {
            return scimError("The 'schemas' type in this request is not supported.", Optional.of(501));
        }

        List<Group> grouplist = groupRepository.findById(id);

        if (grouplist.isEmpty()) {
            return scimError("Group '" + id + "' was not found.", Optional.of(404));
        }
        Group group = grouplist.getFirst();

        Map<String, Object> groupMapOperations = MapConverter.getMapOperations(payload);
        group.update(groupMapOperations, groupEntityMapper);

        String operation = groupMapOperations.get("operation").toString();

        groupMapOperations.forEach((key, value) -> {

            if (key.equals("members")) {
                List<Map<String, Object>> members = (List<Map<String, Object>>) value;
                List<String> userIds = members.stream().map(member ->
                        member.get("value")
                                .toString()).toList();

                Map<String, String> userNames = userRepository.findUserNamesByIdsMap(userIds);
                List<GroupMembership> groupMembershipsToAdd = new ArrayList<>();
                List<GroupMembership> groupMembershipsToRemove = new ArrayList<>();

                members.forEach(member -> {

                    String userId = member.get("value").toString();

                    if (operation.equals("Add")) {
                        if (groupMembershipRepository.existsByGroupIdAndUserId(group.id, userId)) {
                            return;
                        }
                        GroupMembership gm = new GroupMembership();
                        gm.id = UUID.randomUUID().toString();
                        gm.groupId = id;
                        gm.userId = userId;
                        gm.groupDisplay = group.displayName;
                        gm.userDisplay = userNames.get(userId);
                        groupMembershipsToAdd.add(gm);
                    } else if (operation.equals("Remove")) {
                        if (!groupMembershipRepository.existsByGroupIdAndUserId(group.id, userId)) {
                            return;
                        }
                        Page<GroupMembership> groupMembership = groupMembershipRepository.findByGroupIdAndUserId(id, userId, PageRequestBuilder.build());

                        groupMembershipsToRemove.addAll(groupMembership.getContent());
                    }
                });
                if (!groupMembershipsToAdd.isEmpty())
                    groupMembershipRepository.saveAll(groupMembershipsToAdd);
                if (!groupMembershipsToRemove.isEmpty())
                    groupMembershipRepository.deleteAll(groupMembershipsToRemove);
            }
        });

        groupRepository.save(group);
        group.setGroupMemberships(
                groupMembershipRepository
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
            Group group = groupRepository.findById(id).getFirst();
            if (group == null) {
                response.setStatus(404);
                return scimError("Group not found", Optional.of(404));
            }
            Page<GroupMembership> toDelete = groupMembershipRepository.findByGroupId(id, PageRequest.of(0, Integer.MAX_VALUE));
            groupMembershipRepository.deleteAll(toDelete);
            groupRepository.delete(group);
            response.setStatus(204);
            return group.toScimResource();
        } catch (Exception e) {
            response.setStatus(404);
            return scimError("Group not found", Optional.of(404));
        }
    }
}
