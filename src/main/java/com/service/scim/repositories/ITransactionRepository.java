package com.service.scim.repositories;

import com.service.scim.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interface for the {@link Transaction} repositories
 */
@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * Gets a single resource from the repositories, matching the given ID
     * @param id The ID to search for
     * @return The instance of {@link Transaction} found
     */
    List<Transaction> findById(String id);
}
