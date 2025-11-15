package jsonparser.lexer;

import jsonparser.util.*;
import java.util.*;

public class Lexer {
    private final CharReader reader;

    public Lexer(String input) {
        this.reader = new CharReader(input);
    }

    public Token nextToken() {
        skipWhitespace();

        if (!reader.hasNext()) {
            Position pos = reader.getPosition();
            return new Token(TokenType.EOF, null, pos.getLine(), pos.getColumn());
        }

        char c = reader.peek();
        Position pos = reader.getPosition();

        switch (c) {
            case '{':
                reader.advance();
                return new Token(TokenType.LEFT_BRACE, "{", pos.getLine(), pos.getColumn());
            case '}':
                reader.advance();
                return new Token(TokenType.RIGHT_BRACE, "}", pos.getLine(), pos.getColumn());
            case '[':
                reader.advance();
                return new Token(TokenType.LEFT_BRACKET, "[", pos.getLine(), pos.getColumn());
            case ']':
                reader.advance();
                return new Token(TokenType.RIGHT_BRACKET, "]", pos.getLine(), pos.getColumn());
            case ',':
                reader.advance();
                return new Token(TokenType.COMMA, ",", pos.getLine(), pos.getColumn());
            case ':':
                reader.advance();
                return new Token(TokenType.COLON, ":", pos.getLine(), pos.getColumn());
            case '"':
                return stringToken();
            default:
                if (isDigit(c) || c == '-') {
                    return numberToken();
                }
                if (isAlpha(c)) {
                    return literalToken();
                }
        }

        throw new LexerException(
                "Unexpected character: '" + c + "'",
                pos.getLine(),
                pos.getColumn()
        );
    }

    private void skipWhitespace() {
        while (reader.hasNext()) {
            char c = reader.peek();
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                reader.advance();
            } else break;
        }
    }

    private Token stringToken() {
        Position pos = reader.getPosition();
        reader.advance(); // skip opening quote

        StringBuilder sb = new StringBuilder();

        while (reader.hasNext()) {
            char c = reader.advance();

            if (c == '"') {
                return new Token(TokenType.STRING, sb.toString(), pos.getLine(), pos.getColumn());
            }

            if (c == '\\') {  // handle escape sequences
                if (!reader.hasNext()) {
                    throw new LexerException("Invalid escape sequence", pos.getLine(), pos.getColumn());
                }
                char escaped = reader.advance();
                switch (escaped) {
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case '/': sb.append('/'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case 'u':
                        sb.append(parseUnicodeEscape(pos));
                        break;
                    default:
                        throw new LexerException(
                                "Invalid escape sequence: \\" + escaped,
                                pos.getLine(),
                                pos.getColumn()
                        );
                }
            } else {
                sb.append(c);
            }
        }

        throw new LexerException(
                "Unterminated string",
                pos.getLine(),
                pos.getColumn()
        );
    }

    private char parseUnicodeEscape(Position pos) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (!reader.hasNext()) {
                throw new LexerException("Incomplete unicode escape", pos.getLine(), pos.getColumn());
            }
            hex.append(reader.advance());
        }

        try {
            return (char) Integer.parseInt(hex.toString(), 16);
        } catch (Exception e) {
            throw new LexerException("Invalid unicode escape: \\u" + hex, pos.getLine(), pos.getColumn());
        }
    }

    private Token numberToken() {
        Position pos = reader.getPosition();
        StringBuilder sb = new StringBuilder();

        char c = reader.peek();
        if (c == '-') {
            sb.append(reader.advance());
        }

        if (!reader.hasNext())
            throw new LexerException("Invalid number", pos.getLine(), pos.getColumn());

        c = reader.peek();
        if (!isDigit(c))
            throw new LexerException("Invalid number start", pos.getLine(), pos.getColumn());

        if (c == '0') {
            sb.append(reader.advance());
            if (reader.hasNext() && isDigit(reader.peek())) {
                throw new LexerException("Leading zeros not allowed", pos.getLine(), pos.getColumn());
            }
        } else {
            while (reader.hasNext() && isDigit(reader.peek())) {
                sb.append(reader.advance());
            }
        }

        if (reader.hasNext() && reader.peek() == '.') {
            sb.append(reader.advance());
            if (!reader.hasNext() || !isDigit(reader.peek())) {
                throw new LexerException("Invalid decimal format", pos.getLine(), pos.getColumn());
            }
            while (reader.hasNext() && isDigit(reader.peek())) {
                sb.append(reader.advance());
            }
        }

        if (reader.hasNext() && (reader.peek() == 'e' || reader.peek() == 'E')) {
            sb.append(reader.advance());
            if (reader.peek() == '+' || reader.peek() == '-') {
                sb.append(reader.advance());
            }
            if (!reader.hasNext() || !isDigit(reader.peek())) {
                throw new LexerException("Invalid exponent", pos.getLine(), pos.getColumn());
            }
            while (reader.hasNext() && isDigit(reader.peek())) {
                sb.append(reader.advance());
            }
        }

        return new Token(TokenType.NUMBER, sb.toString(), pos.getLine(), pos.getColumn());
    }

    private Token literalToken() {
        Position pos = reader.getPosition();

        StringBuilder sb = new StringBuilder();
        while (reader.hasNext() && isAlpha(reader.peek())) {
            sb.append(reader.advance());
        }

        String literal = sb.toString();

        return switch (literal) {
            case "true" -> new Token(TokenType.TRUE, literal, pos.getLine(), pos.getColumn());
            case "false" -> new Token(TokenType.FALSE, literal, pos.getLine(), pos.getColumn());
            case "null" -> new Token(TokenType.NULL, literal, pos.getLine(), pos.getColumn());
            default ->
                    throw new LexerException("Invalid literal: " + literal, pos.getLine(), pos.getColumn());
        };
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z');
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        Token token;
        do {
            token = nextToken();
            tokens.add(token);
        } while (token.getType() != TokenType.EOF);

        return tokens;
    }
}
