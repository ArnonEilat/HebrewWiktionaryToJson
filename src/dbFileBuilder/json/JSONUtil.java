package dbFileBuilder.json;


public class JSONUtil {
    private JSONUtil() {
    }

    /**
     * Remove all the<tt>\n</tt> (new line sign) from <tt>text</tt>.
     * @param text to remove newline sign from.
     * @return same String as <tt>text</tt> but without newline sings.
     */
    protected static String removeNewLine(String text) {
        do {
            text = text.replaceAll("\n", "");
        } while (text.contains("\n"));
        return text;
    }
}

