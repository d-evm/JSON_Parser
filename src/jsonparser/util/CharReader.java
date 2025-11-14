package jsonparser.util;

// Reads characters one-by-one and updates the Position.
public class CharReader {
    private final String input;
    private int index = 0;
    private final Position position = new Position();

    public CharReader(String input) {
        this.input = input;
    }

    public boolean hasNext() {
        return index < input.length();
    }

    public char peek() {
        return hasNext() ? input.charAt(index) : '\0';
    }

    public char advance() {
        char c = peek();
        if (hasNext()) {
            index++;
            position.advance(c);
        }
        return c;
    }

    public Position getPosition() {
        return position;
    }
}
