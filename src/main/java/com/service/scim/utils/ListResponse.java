package com.service.scim.utils;

import com.service.scim.models.BaseModel;
import com.service.scim.models.mapper.strategies.formatter.IScimResourceFormatter;
import com.service.scim.models.mapper.strategies.formatter.ListResponseScimResourceFormatter;
import com.service.scim.models.mapper.strategies.groupmembership.IGroupMembershipAssigner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Returns an array of SCIM resources into a Query Resource
 */

public class ListResponse<T extends BaseModel> {
    private final List<T> list;
    private final int startIndex;
    private final int count;
    private final int totalResults;
    private final IGroupMembershipAssigner<T> membershipAssigner;
    private final IScimResourceFormatter<ListResponse<T>> listResponseScimResourceFormatter;

    private ListResponse(Builder<T> builder) {
        this.list = builder.list;
        this.startIndex = builder.startIndex;
        this.count = builder.count;
        this.totalResults = builder.totalResults;
        this.membershipAssigner = builder.membershipAssigner;
        this.listResponseScimResourceFormatter = builder.listResponseScimResourceFormatter;
    }

    public static class Builder<T extends BaseModel> {
        private List<T> list = new ArrayList<>();
        private int startIndex = 1;
        private int count = 0;
        private int totalResults = 0;
        private IGroupMembershipAssigner<T> membershipAssigner;
        private final IScimResourceFormatter<ListResponse<T>> listResponseScimResourceFormatter = new ListResponseScimResourceFormatter<T>();

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

    public List<T> getList() {
        return list;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getCount() {
        return count;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public IGroupMembershipAssigner<T> getMembershipAssigner() {
        return membershipAssigner;
    }

    /**
     * Converts the object to an SCIM resource in JSON format
     */
    public Map<String, Object> toScimResource() {
        return listResponseScimResourceFormatter.toScimResource(this);
    }
}
