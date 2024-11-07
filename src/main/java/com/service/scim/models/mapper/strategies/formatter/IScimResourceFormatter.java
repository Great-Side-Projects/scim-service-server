package com.service.scim.models.mapper.strategies.formatter;

import java.util.Map;

public interface IScimResourceFormatter<T> {
    Map<String, Object> toScimResource(T entity);
}
