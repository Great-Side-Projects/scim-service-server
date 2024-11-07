package com.service.scim.utils;

import com.service.scim.models.BaseModel;
import com.service.scim.models.mapper.strategies.groupmembership.IGroupMembershipAssigner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Returns an array of SCIM resources into a Query Resource
 */
public class ListResponse<T extends BaseModel> {
    private final List<T> list;
    private final int startIndex;
    private final int count;
    private final int totalResults;
    private final IGroupMembershipAssigner<T> membershipAssigner;

    private ListResponse(Builder<T> builder) {
        this.list = builder.list;
        this.startIndex = builder.startIndex;
        this.count = builder.count;
        this.totalResults = builder.totalResults;
        this.membershipAssigner = builder.membershipAssigner;
    }

    public static class Builder<T extends BaseModel> {
        private List<T> list = new ArrayList<>();
        private int startIndex = 1;
        private int count = 0;
        private int totalResults = 0;
        private IGroupMembershipAssigner<T> membershipAssigner;

        public Builder<T> withList(List<T> list) {
            this.list = list;
            return this;
        }

        public Builder<T> withStartIndex(int startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        public Builder<T> withCount(int count) {
            this.count = count;
            return this;
        }

        public Builder<T> withTotalResults(int totalResults) {
            this.totalResults = totalResults;
            return this;
        }

        public Builder<T> withMembershipAssigner(IGroupMembershipAssigner<T> membershipAssigner) {
            this.membershipAssigner = membershipAssigner;
            return this;
        }

        public ListResponse<T> build() {
            return new ListResponse<>(this);
        }
    }

    /**
     * Converts the object to a SCIM resource in JSON format
     */
    public Map<String, Object> toScimResource() {
        Map<String, Object> scimResource = new HashMap<>();
        scimResource.put("schemas", List.of("urn:ietf:params:scim:api:messages:2.0:ListResponse"));
        scimResource.put("totalResults", this.totalResults);
        scimResource.put("startIndex", this.startIndex);
        scimResource.put("itemsPerPage", Math.min(this.count, this.totalResults));

        List resources = this.list.stream()
                .map(T::toScimResource)
                .collect(Collectors.toList());

        if (membershipAssigner != null) {
            resources = membershipAssigner.assignMemberships(resources, this.list);
        }

        scimResource.put("Resources", resources);
        return scimResource;
    }
}
