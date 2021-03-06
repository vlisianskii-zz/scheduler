package org.ton.model;

import java.util.HashMap;

public class SafeMap extends HashMap<String, Object> {
    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        return (T) get(key);
    }
}
