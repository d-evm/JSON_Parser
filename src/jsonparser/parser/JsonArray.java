package jsonparser.parser;

import java.util.ArrayList;
import java.util.List;

public class JsonArray implements JsonValue {
    private final List<JsonValue> values = new ArrayList<>();

    public void add(JsonValue v) {
        values.add(v);
    }

    public List<JsonValue> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
