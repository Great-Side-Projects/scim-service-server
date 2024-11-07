package com.service.scim.models.mapper.strategies.groupmembership;

import com.service.scim.models.BaseModel;
import java.util.List;
import java.util.Map;

public interface IGroupMembershipAssigner <T extends BaseModel> {
    List<Map<String, Object>> assignMemberships(List<Map<String, Object>> resources, List<T> list);
}
