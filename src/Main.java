import jsonparser.lexer.*;
import jsonparser.parser.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String json = "{ \"a\": 10, \"b\": [1, 2, 3], \"c\": true, \"d\": null }";

        // Step 1: Tokenize
        Lexer lexer = new Lexer(json);
        List<Token> tokens = lexer.tokenize();

        System.out.println("=== TOKENS ===");
        for (Token t : tokens) {
            System.out.println(t);
        }

        // Step 2: Parse
        Parser parser = new Parser(tokens);
        JsonValue value = parser.parse();

        System.out.println("\n=== PARSED VALUE ===");
        System.out.println(value);
    }
}
