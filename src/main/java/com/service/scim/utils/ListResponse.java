package com.service.scim.utils;

import com.service.scim.models.BaseModel;
import com.service.scim.models.GroupMembership;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Returns an array of SCIM resources into a Query Resource
 */
public class ListResponse<T extends BaseModel> {
    private List<T> list;
    private List<GroupMembership> groupMemberships;
    private int startIndex;
    private int count;
    private int totalResults;

    public ListResponse(){
        this.list = new ArrayList<>();
        this.startIndex = 1;
        this.count = 0;
        this.totalResults = 0;
    }

    public ListResponse(
            List<T> list,
            Optional<Integer> startIndex,
            Optional<Integer> count,
            Optional<Integer> totalResults,
            List<GroupMembership> groupMemberships)
    {
        this.list = list;
        this.groupMemberships = groupMemberships;
    // startIndex.orElse checks for optional values
        this.startIndex = startIndex.orElse(1);
        this.count = count.orElse(0);
        this.totalResults = totalResults.orElse(0);
    }

    /**
     * @return JSON {@link Map} of {@link ListResponse} object
     */
    public HashMap<String, Object> toScimResource(){
        HashMap<String, Object> returnValue = new HashMap<>();

        List<String> schemas = new ArrayList<>();
        schemas.add("urn:ietf:params:scim:api:messages:2.0:ListResponse");
        returnValue.put("schemas", schemas);
        returnValue.put("totalResults", this.totalResults);
        returnValue.put("startIndex", this.startIndex);


        List<Map> resources = this.list.stream().map(T::toScimResource).collect(Collectors.toList());

        //add group memberships to each user resource
        if(groupMemberships != null){
            for(Map<String, Object> resource : resources){
                List<GroupMembership> memberships = groupMemberships.stream()
                        .filter(gm -> gm.userId.equals(resource.get("id")))
                        .toList();
                List<Map> groupMemberships = memberships.stream().map(GroupMembership::toUserScimResource).collect(Collectors.toList());
                resource.put("groups", groupMemberships);
            }
        }

        if (this.count > this.totalResults) {
            this.count = this.totalResults;
        }

        if (this.count != 0) {
            returnValue.put("itemsPerPage", this.count);
        }

        returnValue.put("Resources", resources);

        return returnValue;
    }
}
