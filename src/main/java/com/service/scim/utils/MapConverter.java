package com.service.scim.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapConverter {

    public static Map<String, Object> getMapOperations(Map<String, Object> mapResource){

        List<Map> operations = (List) mapResource.get("Operations");
        return  operations.stream()
                .collect(HashMap::new, (m, v) -> {
                    String path = v.get("path").toString();
                    Object value = v.get("value");
                    m.put(path, convertValue(path, value));
                }, HashMap::putAll);
    }

    private static Object convertValue(String path, Object value) {
        if ("active".equals(path)) {
            return Boolean.parseBoolean(value.toString());
        }
        return value.toString();
    }

}
