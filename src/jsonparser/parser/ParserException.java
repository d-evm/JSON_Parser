package jsonparser.parser;

public class ParserException extends RuntimeException {

    private final int line;
    private final int column;
    private final String snippet;

    public ParserException(String message, int line, int column, String snippet) {
        super(message);
        this.line = line;
        this.column = column;
        this.snippet = snippet;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Syntax Error: ").append(super.getMessage()).append("\n");
        sb.append("Line ").append(line).append(", Column ").append(column).append("\n");

        if (snippet != null) {
            sb.append(snippet).append("\n");

            // Add caret indicator
            for (int i = 1; i < column; i++) sb.append(" ");
            sb.append("^\n");
        }

        return sb.toString();
    }
}
