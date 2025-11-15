package jsonparser.util;

public class PrettyErrorFormatter {

    /**
     * Pretty prints an error so it points to the exact location.
     */
    public static String format(String json, String message) {
        StringBuilder sb = new StringBuilder();

        sb.append("ERROR: ").append(message).append("\n\n");
        sb.append(json).append("\n");

        // Try to extract line:column from message
        int line = 1;
        int col = 1;

        try {
            String[] parts = message.split(" ");
            for (String p : parts) {
                if (p.contains(":")) {
                    String[] lc = p.split(":");
                    line = Integer.parseInt(lc[0]);
                    col = Integer.parseInt(lc[1]);
                    break;
                }
            }
        } catch (Exception ignored) {}

        // Build caret underline
        sb.append(" ".repeat(Math.max(0, col - 1)));
        sb.append("^\n");

        return sb.toString();
    }
}
