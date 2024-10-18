package com.service.scim.models.mapper;

import com.service.scim.models.User;

public interface IMapStrategy {
    void applyUpdate(User user, String field, Object value);
}
