package com.service.scim.services.factories;

import static org.junit.jupiter.api.Assertions.*;
import com.service.scim.models.mapper.strategies.patchoperation.AddMembersStrategy;
import com.service.scim.models.mapper.strategies.patchoperation.IPatchOperationStrategy;
import com.service.scim.models.mapper.strategies.patchoperation.RemoveMembersStrategy;
import com.service.scim.repositories.IGroupMembershipRepository;
import com.service.scim.repositories.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PatchOperationFactoryTest {

    @Mock
    private IGroupMembershipRepository groupMembershipRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private PatchOperationFactory patchOperationFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patchOperationFactory = new PatchOperationFactory(groupMembershipRepository, userRepository);
    }

    @Test
    void getStrategyReturnsAddMembersStrategy() {
        IPatchOperationStrategy strategy = patchOperationFactory.getStrategy("Add");
        assertInstanceOf(AddMembersStrategy.class, strategy);
    }

    @Test
    void getStrategyReturnsRemoveMembersStrategy() {
        IPatchOperationStrategy strategy = patchOperationFactory.getStrategy("Remove");
        assertInstanceOf(RemoveMembersStrategy.class, strategy);
    }

    @Test
    void getStrategyThrowsExceptionForUnsupportedOperation() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            patchOperationFactory.getStrategy("Update");
        });
        assertEquals("Operation not supported: Update", exception.getMessage());
    }

    @Test
    void getStrategyHandlesNullOperation() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            patchOperationFactory.getStrategy(null);
        });
        assertEquals("Operation not supported: null", exception.getMessage());
    }
}