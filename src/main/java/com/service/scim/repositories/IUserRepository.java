package com.service.scim.repositories;

import com.service.scim.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface for the {@link User} repositories
 */
@Repository
public interface IUserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    /**
     * Gets a single resource from the repositories, matching the given ID
     *
     * @param id The ID to search for
     * @return The instance of {@link User} found
     */
    Optional<User> findById(String id);


    // get only usernames by ids , result in map id, username
    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    List<User> findByIds(@Param("ids") List<String> ids);

    default Map<String, String> findUserNamesByIdsMap(List<String> ids) {
        return findByIds(ids).stream().collect(
                java.util.stream.Collectors.toMap(User::getId, User::getUserName));
    }
}
