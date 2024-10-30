package com.service.scim.utils;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PageRequestBuilderTest {

    @Test
    void buildReturnsPageRequestWithDefaultValues() {
        Map<String, String> params = new HashMap<>();
        PageRequest result = PageRequestBuilder.build(params);

        assertEquals(0, result.getPageNumber());
        assertEquals(100, result.getPageSize());
    }

    @Test
    void buildReturnsPageRequestWithProvidedValues() {
        Map<String, String> params = new HashMap<>();
        params.put("count", "50");
        params.put("startIndex", "10");
        PageRequest result = PageRequestBuilder.build(params);

        assertEquals(9, result.getPageNumber());
        assertEquals(50, result.getPageSize());
    }

    @Test
    void buildHandlesNegativeStartIndex() {
        Map<String, String> params = new HashMap<>();
        params.put("startIndex", "-5");
        PageRequest result = PageRequestBuilder.build(params);

        assertEquals(0, result.getPageNumber());
        assertEquals(100, result.getPageSize());
    }

    @Test
    void buildHandlesNonNumericValues() {
        Map<String, String> params = new HashMap<>();
        params.put("count", "abc");
        params.put("startIndex", "xyz");
        assertThrows(NumberFormatException.class, () -> PageRequestBuilder.build(params));
    }

    @Test
    void buildWithNoParamsReturnsMaxPageRequest() {
        PageRequest result = PageRequestBuilder.build();

        assertEquals(0, result.getPageNumber());
        assertEquals(Integer.MAX_VALUE, result.getPageSize());
    }
}
