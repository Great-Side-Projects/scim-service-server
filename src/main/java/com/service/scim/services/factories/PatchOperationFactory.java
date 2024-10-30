package com.service.scim.services.factories;

import com.service.scim.models.mapper.strategies.patchoperation.AddMembersStrategy;
import com.service.scim.models.mapper.strategies.patchoperation.IPatchOperationStrategy;
import com.service.scim.models.mapper.strategies.patchoperation.RemoveMembersStrategy;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.repositories.IUserRepository;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;

@Component
public class PatchOperationFactory {

    private final Map<String, IPatchOperationStrategy> strategies;

    public PatchOperationFactory(IGroupMembershipRepository groupMembershipRepository, IUserRepository userRepository) {
        this.strategies = Map.of(
                "Add", new AddMembersStrategy(groupMembershipRepository, userRepository),
                "Remove", new RemoveMembersStrategy(groupMembershipRepository)
        );
    }

    public IPatchOperationStrategy getStrategy(String operation) {
        if (operation == null) {
            throw new UnsupportedOperationException("Operation not supported: null");
        }
        return Optional.ofNullable(strategies.get(operation))
                .orElseThrow(() -> new UnsupportedOperationException(
                        "Operation not supported: " + operation));
    }
}
