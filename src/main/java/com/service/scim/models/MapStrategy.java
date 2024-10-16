package com.service.scim.models;

public interface MapStrategy {
    void applyUpdate(User user, String Field, Object value);
}
