package com.service.scim.models;

import java.util.Map;

/**
 * The base schema for models
 */
public abstract class BaseModel {
    public abstract Map toScimResource();
}
