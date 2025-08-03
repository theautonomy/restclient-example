package com.example.app;

import java.util.*;

/**
 * Simple HttpHeaders class to manage headers
 */
public class HttpHeaders {
    private final Map<String, List<String>> headers = new HashMap<>();
    
    public void add(String name, String value) {
        headers.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
    }
    
    public void set(String name, String value) {
        headers.put(name, Arrays.asList(value));
    }
    
    public List<String> get(String name) {
        return headers.getOrDefault(name, Collections.emptyList());
    }
    
    public String getFirst(String name) {
        List<String> values = get(name);
        return values.isEmpty() ? null : values.get(0);
    }
    
    public Set<String> keySet() {
        return headers.keySet();
    }
}
