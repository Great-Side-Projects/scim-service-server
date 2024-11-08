package com.service.scim.models.mapper.strategies.formatter;

import com.service.scim.models.BaseModel;
import com.service.scim.utils.ListResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListResponseScimResourceFormatter<T extends BaseModel> implements IScimResourceFormatter<ListResponse<T>> {

    @Override
    public Map<String, Object> toScimResource(ListResponse<T> listResponse) {
        Map<String, Object> scimResource = new HashMap<>();
        scimResource.put("schemas", List.of("urn:ietf:params:scim:api:messages:2.0:ListResponse"));
        scimResource.put("totalResults", listResponse.getTotalResults());
        scimResource.put("startIndex", listResponse.getStartIndex());
        scimResource.put("itemsPerPage", Math.min(listResponse.getCount(), listResponse.getTotalResults()));

        List resources = listResponse.getList().stream()
                .map(T::toScimResource)
                .collect(Collectors.toList());

        if (listResponse.getMembershipAssigner() != null) {
            resources = listResponse.getMembershipAssigner().assignMemberships(resources, listResponse.getList());
        }

        scimResource.put("Resources", resources);
        return scimResource;
    }
}
