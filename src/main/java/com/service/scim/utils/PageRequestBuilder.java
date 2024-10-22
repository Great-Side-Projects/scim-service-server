package com.service.scim.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class PageRequestBuilder {

    public static PageRequest build(Map<String, String> params) {
        int count = Integer.parseInt(params.getOrDefault("count", "100"));
        int startIndex = Integer.parseInt(params.getOrDefault("startIndex", "1"));
        startIndex = Math.max(startIndex - 1, 0);  // Ensure startIndex is not negative

        return PageRequest.of(startIndex, count);
    }

    public static PageRequest build() {
        return PageRequest.of(0, Integer.MAX_VALUE);
    }
}
