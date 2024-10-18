package com.service.scim.repositories;

import com.service.scim.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interface for the {@link Request} repositories
 */
@Repository
public interface IRequestRepository extends JpaRepository<Request, Long> {
    /**
     * Gets a single resource from the repositories, matching the given ID
     * @param id The ID to search for
     * @return The instance of {@link Request} found
     */
    List<Request> findById(String id);
}
