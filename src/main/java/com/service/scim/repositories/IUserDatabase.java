package com.service.scim.repositories;

import com.service.scim.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

/**
 * Interface for the {@link User} repositories
 */
@Repository
public interface IUserDatabase extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    /**
     * Gets a single resource from the repositories, matching the given ID
     * @param id The ID to search for
     * @return The instance of {@link User} found
     */
    List<User> findById(String id);
}
