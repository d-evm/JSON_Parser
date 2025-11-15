import jsonparser.lexer.*;

public class Main {
    public static void main(String[] args) {
        String json = "{ \"a\": 10, \"b\": [1,2] }";
        Lexer lexer = new Lexer(json);

        Token token;
        do {
            token = lexer.nextToken();
            System.out.println(token);
        } while (token.getType() != TokenType.EOF);
    }
}
