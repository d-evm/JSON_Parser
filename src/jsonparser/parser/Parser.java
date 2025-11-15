package jsonparser.parser;

import jsonparser.lexer.*;
import jsonparser.util.*;

import java.util.*;

public class Parser {

    private final List<Token> tokens;
    private final String originalInput;
    private int index = 0;

    public Parser(List<Token> tokens, String originalInput) {
        this.tokens = tokens;
        this.originalInput = originalInput;
    }

    // Convenience constructor (parser without original input)
    public Parser(List<Token> tokens) {
        this(tokens, null);
    }

    // Entry point
    public JsonValue parse() {
        JsonValue value = parseValue();
        expect(TokenType.EOF);
        return value;
    }

    // ---------- VALUE ----------
    private JsonValue parseValue() {
        Token t = peek();

        switch (t.getType()) {
            case LEFT_BRACE:
                return parseObject();
            case LEFT_BRACKET:
                return parseArray();
            case STRING:
                return new JsonPrimitive(consume().getValue());
            case NUMBER:
                return new JsonPrimitive(parseNumberLiteral(consume().getValue()));
            case TRUE:
                consume();
                return new JsonPrimitive(true);
            case FALSE:
                consume();
                return new JsonPrimitive(false);
            case NULL:
                consume();
                return new JsonPrimitive(null);
            default:
                throw error("Unexpected token: " + t.getType());
        }
    }

    // ---------- OBJECT ----------
    private JsonObject parseObject() {
        expect(TokenType.LEFT_BRACE);
        JsonObject obj = new JsonObject();

        if (peek().getType() == TokenType.RIGHT_BRACE) {
            consume();
            return obj;
        }

        do {
            Token key = expect(TokenType.STRING);
            expect(TokenType.COLON);
            JsonValue value = parseValue();
            obj.put(key.getValue(), value);
        } while (tryConsume(TokenType.COMMA));

        expect(TokenType.RIGHT_BRACE);
        return obj;
    }

    // ---------- ARRAY ----------
    private JsonArray parseArray() {
        expect(TokenType.LEFT_BRACKET);
        JsonArray arr = new JsonArray();

        if (peek().getType() == TokenType.RIGHT_BRACKET) {
            consume();
            return arr;
        }

        do {
            arr.add(parseValue());
        } while (tryConsume(TokenType.COMMA));

        expect(TokenType.RIGHT_BRACKET);
        return arr;
    }

    // ---------- NUMBER ----------
    private Double parseNumberLiteral(String num) {
        return Double.valueOf(num);
    }

    // ---------- TOKEN HELPERS ----------
    private Token consume() {
        return tokens.get(index++);
    }

    private Token expect(TokenType type) {
        Token t = peek();
        if (t.getType() != type) {
            throw error("Expected " + type + " but got " + t.getType());
        }
        return consume();
    }

    private boolean tryConsume(TokenType type) {
        if (peek().getType() == type) {
            consume();
            return true;
        }
        return false;
    }

    private Token peek() {
        return tokens.get(index);
    }

    // ---------- ERROR HANDLING ----------
    private ParserException error(String message) {
        Token t = peek();
        String snippet = buildSnippet(t.getLine());

        String fullMessage = message + " at " + t.getLine() + ":" + t.getColumn();

        return new ParserException(fullMessage, t.getLine(), t.getColumn(), snippet);
    }

    private String buildSnippet(int errorLine) {
        if (originalInput == null) return null;

        String[] lines = originalInput.split("\n");
        if (errorLine <= 0 || errorLine > lines.length) return null;

        return lines[errorLine - 1];
    }
}
