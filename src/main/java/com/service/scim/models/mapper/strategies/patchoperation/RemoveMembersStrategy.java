package com.service.scim.models.mapper.strategies.patchoperation;

import com.service.scim.models.Group;
import com.service.scim.models.GroupMembership;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.utils.PageRequestBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RemoveMembersStrategy implements IPatchOperationStrategy<Group> {

    private final IGroupMembershipRepository groupMembershipRepository;

    public RemoveMembersStrategy(IGroupMembershipRepository groupMembershipRepository) {
        this.groupMembershipRepository = groupMembershipRepository;
    }

    @Override
    public void execute(Group entity, List<Map<String, Object>> members) {

        if (members == null || members.isEmpty())
            return;

        List<GroupMembership> membershipsToRemove = members.stream()
                .map(member -> {
                    String userId = member.get("value").toString();
                    if (!groupMembershipRepository.existsByGroupIdAndUserId(entity.id, userId)) {
                        return Collections.<GroupMembership>emptyList();
                    }
                    return groupMembershipRepository
                            .findByGroupIdAndUserId(entity.id, userId, PageRequestBuilder.build())
                            .getContent();
                })
                .flatMap(Collection::stream)
                .toList();

        if (!membershipsToRemove.isEmpty()) {
            groupMembershipRepository.deleteAll(membershipsToRemove);
        }
    }
}
