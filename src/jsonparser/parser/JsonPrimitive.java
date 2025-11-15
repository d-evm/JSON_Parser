package jsonparser.parser;

public class JsonPrimitive implements JsonValue {
    private final Object value;

    public JsonPrimitive(Object value) {
        this.value = value;
    }

    public Object get() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
