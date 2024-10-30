package com.service.scim.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MapConverterTest {

    @Test
    void getMapOperationsReturnsCorrectMap() {
        Map<String, Object> mapResource = new HashMap<>();
        Map<String, Object> operation = new HashMap<>();
        operation.put("path", "active");
        operation.put("value", "true");
        operation.put("op", "replace");
        mapResource.put("Operations", List.of(operation));

        Map<String, Object> result = MapConverter.getMapOperations(mapResource);

        assertNotNull(result);
        assertEquals(true, result.get("active"));
        assertEquals("replace", result.get("operation"));
    }

    @Test
    void getMapOperationsHandlesEmptyOperations() {
        Map<String, Object> mapResource = new HashMap<>();
        mapResource.put("Operations", List.of());

        Map<String, Object> result = MapConverter.getMapOperations(mapResource);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getMapOperationsHandlesNullValue() {
        Map<String, Object> mapResource = new HashMap<>();
        Map<String, Object> operation = new HashMap<>();
        operation.put("path", "displayName");
        operation.put("value", null);
        operation.put("op", "Replace");
        mapResource.put("Operations", List.of(operation));

        Map<String, Object> result = MapConverter.getMapOperations(mapResource);

        assertNotNull(result);
        assertNull(result.get("displayName"));
        assertEquals("Replace", result.get("operation"));
    }
}
