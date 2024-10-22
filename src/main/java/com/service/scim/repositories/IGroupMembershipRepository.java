package com.service.scim.repositories;

import com.service.scim.models.GroupMembership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interface for the {@link GroupMembership} repositories
 */
@Repository
public interface IGroupMembershipRepository extends JpaRepository<GroupMembership, Long> {
    /**
     * Gets a single resource from the repositories, matching the given ID
     * @param id The ID to search for
     * @return The instance of {@link GroupMembership} found
     */
    List<GroupMembership> findById(String id);

    /**
     * Searches and returns all instances of {@link GroupMembership} that match a given group ID
     * @param groupId The group ID to search
     * @param pagable A pageable object, usually a {@link org.springframework.data.domain.PageRequest}
     * @return A {@link Page} object with the found {@link GroupMembership} instances
     */
    @Query("SELECT gm FROM GroupMembership gm WHERE gm.groupId = :groupId")
    Page<GroupMembership> findByGroupId(@Param("groupId") String groupId, Pageable pagable);

    @Query("SELECT gm FROM GroupMembership gm WHERE gm.groupId IN (:groupIds)")
    Page<GroupMembership> findByGroupIds(@Param("groupIds")  List<String> groupIds, Pageable pagable);

    @Query("SELECT gm FROM GroupMembership gm WHERe gm.userId IN(:userIds)")
    Page<GroupMembership> findByUserIds(@Param("userIds") List<String> userIds, Pageable pageable);
    //exists user bay user id bool
    @Query("SELECT CASE WHEN COUNT(gm) > 0 THEN true ELSE false END FROM GroupMembership gm WHERE gm.userId = :userId AND gm.groupId = :groupId")
    boolean existsByGroupIdAndUserId(@Param("groupId") String groupId, @Param("userId") String userId);

    /**
     * Searches and returns all instances of {@link GroupMembership} that match a given group ID and userId
     * @param groupId The group ID to search
     * @param userId The userId to search
     * @param pagable A pageable object, usually a {@link org.springframework.data.domain.PageRequest}
     * @return A {@link Page} object with the found {@link GroupMembership} instances
     */
    @Query("SELECT gm FROM GroupMembership gm WHERE gm.groupId = :groupId AND gm.userId = :userId")
    Page<GroupMembership> findByGroupIdAndUserId(@Param("groupId") String groupId, @Param("userId") String userId, Pageable pagable);

    @Modifying
    @Query("UPDATE GroupMembership gm SET gm.groupDisplay = :groupDisplay WHERE gm.groupId = :groupId")
    int updateGroupDisplayByGroupId(@Param("groupId") String groupId, @Param("groupDisplay") String groupDisplay);
}
