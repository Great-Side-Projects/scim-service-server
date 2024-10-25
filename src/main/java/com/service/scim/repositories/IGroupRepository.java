package com.service.scim.repositories;

import com.service.scim.models.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface for the {@link Group} repositories
 */
@Repository
public interface IGroupRepository extends JpaRepository<Group, String>, JpaSpecificationExecutor<Group> {
    /**
     * Gets a single resource from the repositories, matching the given ID
     *
     * @param id The ID to search for
     * @return The instance of {@link Group} found
     */
    Optional<Group> findById(String id);

    /**
     * Searches and returns all instances of {@link Group} that match a given userDisplay name
     * @param name The userDisplay name to search
     * @param pagable A pageable object, usually a {@link org.springframework.data.domain.PageRequest}
     * @return A {@link Page} object with the found {@link Group} instances
     */
    @Query("SELECT g FROM Group g WHERE g.displayName = :name")
    Page<Group> findByDisplayName(@Param("name") String name, Pageable pagable);

    /**
     * Checks if a {@link Group} with the given userDisplay name exists
     * @param name The userDisplay name to search
     * @return A boolean value indicating if the {@link Group} exists
     */
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN TRUE ELSE FALSE END FROM Group g WHERE g.displayName = :name")
    Boolean existsByDisplayName(@Param("name") String name);
}
