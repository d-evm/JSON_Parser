package jsonparser.util;

// Tracks line + column numbers for error reporting.
public class Position {
    private int line;
    private int column;

    public Position(){
        this.line = 1;
        this.column = 1;
    }

    public void advance(char c){
        if (c == '\n'){
            line++;
            column = 1;
        } else {
            column++;
        }
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
