package jsonparser.lexer;

public class LexerException extends RuntimeException {

    private final int line;
    private final int column;

    public LexerException(String message, int line, int column) {
        super(message + " at line " + line + ", column " + column);
        this.line = line;
        this.column = column;
    }
}
