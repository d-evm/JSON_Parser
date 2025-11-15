import jsonparser.lexer.*;
import jsonparser.parser.*;
import jsonparser.util.*;

import java.util.*;

public class TestMain {

    public static void main(String[] args) {

        List<String> tests = List.of(
                // ---- VALID CASES ----
                "{\"name\":\"John\", \"age\":30}",
                "{\"escaped\":\"Hello \\\"World\\\"\"}",
                "{\"unicode\":\"Hi \\u0041\"}",
                "[1, 2, 3, 4]",
                "[true, false, null]",
                "{\"nested\": {\"a\": 1, \"b\": [10, 20, 30]}}",
                "{\"float\": 12.45, \"exp\": 1e10}",
                "{\"mix\": [1, \"text\", null, false, {\"x\":10}]}",
                "[]",
                "{}",

                // ---- ERROR CASES ----
                "{\"name\": \"John}",                       // Unterminated string
                "{name: 123}",                               // Missing quotes in key
                "{\"a\": 01}",                                // Leading zero error
                "{,}",                                       // Random comma
                "{\"a\": tru}"                               // Invalid literal
        );

        int index = 1;

        for (String json : tests) {
            System.out.println("\n===============================");
            System.out.println("TEST #" + index++);
            System.out.println("Input JSON:");
            System.out.println(json);
            System.out.println("-------------------------------");

            try {
                Lexer lexer = new Lexer(json);
                List<Token> tokens = lexer.tokenize();

                System.out.println("TOKENS:");
                for (Token t : tokens) {
                    System.out.println("  " + t);
                }

                Parser parser = new Parser(tokens);
                Object result = parser.parse();

                System.out.println("\nPARSED OUTPUT:");
                System.out.println(result);

            } catch (LexerException e) {
                System.out.println("\nLEXER ERROR:");
                System.out.println(PrettyErrorFormatter.format(json, e.getMessage()));

            } catch (ParserException e) {
                System.out.println("\nPARSER ERROR:");
                System.out.println(PrettyErrorFormatter.format(json, e.getMessage()));

            } catch (Exception e) {
                System.out.println("\nUNKNOWN ERROR:");
                e.printStackTrace();
            }
        }
    }
}
