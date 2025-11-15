package jsonparser.parser;

import java.util.Map;
import java.util.HashMap;

public class JsonObject implements JsonValue {
    private final Map<String, JsonValue> map = new HashMap<>();

    public void put(String key, JsonValue value) {
        map.put(key, value);
    }

    public Map<String, JsonValue> getMap() {
        return map;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
