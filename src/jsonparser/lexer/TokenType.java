package jsonparser.lexer;

// All possible token types the lexer can produce.
public enum TokenType {
    LEFT_BRACE,     // {
    RIGHT_BRACE,    // }
    LEFT_BRACKET,   // [
    RIGHT_BRACKET,  // ]
    COMMA,          // ,
    COLON,          // :
    STRING,
    NUMBER,
    TRUE,
    FALSE,
    NULL,
    EOF
}
