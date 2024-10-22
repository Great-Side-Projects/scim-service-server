package com.service.scim.models.mapper.strategies.patchoperation;

import java.util.List;
import java.util.Map;

public interface IPatchOperationStrategy<T> {
    void execute(T entity, List<Map<String, Object>> members);
}
