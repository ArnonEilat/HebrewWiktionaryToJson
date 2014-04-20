package dbFileBuilder.wiki;

import java.util.regex.Pattern;
import dbFileBuilder.RegEx;

/**
 * Static utility class to be used by {@link Wiki} class.
 */
public final class Util {

    /**
     * Don't let anyone instantiate this class.
     */
    private Util() {
    }

    /**
     * Remove the last <tt>\n</tt>(new line sign) from <tt>text</tt>.
     *
     * @param text to remove the last <tt>\n</tt> from.
     * @return If <tt>text</tt> does not contain <tt>\n</tt> in its end, the
     * <tt>text</tt> returned as is.<br>
     * Otherwise <tt>text</tt> returned without the <tt>\n</tt> in its end.
     */
    protected static StringBuilder removeNewLineSignFromEnd(StringBuilder text) {
        if (text.charAt(text.length() - 1) == '\n') {
            return new StringBuilder(text.substring(0, text.length() - 1));
        } else {
            return text;
        }
    }

    /**
     * Remove the first <tt>\n</tt> (new line sign) from the beginning of
     * <tt>text</tt>.
     *
     * @param text to remove <tt>\n</tt> from its beginning.
     * @return If <tt>text</tt> does not start with <tt>\n</tt>, the
     * <tt>text</tt> returned as is.<br>
     * Otherwise <tt>text</tt> returned without the <tt>\n</tt> in its start.
     */
    protected static String removeNewLineSignFromStart(String text) {
        if (text.length() == 0) {
            return text;
        }
        if (text.charAt(0) == '\n') {
            return text.substring(1, text.length());
        } else {
            return text;
        }
    }

    /**
     * @param str String to split by new line sign.
     * @return Array strings
     */
    protected static String[] splitByNewLineSign(String str) {
        return str.split("\\n");
    }

    /**
     * Returns true if and only if all the {@link Pattern}s in the
     * {@link Patterns} class not matches.
     *
     * @param text to match against all the patterns in the {@link Patterns}
     * class.
     * @return <tt>true</tt> if <tt>text</tt> is a normal text.
     */
    protected static boolean isNormalText(String text) {
        if (text.matches("^={2}[^=]*={2}") == true) {
            return false;
        }
        if (text.matches("^={3}[^=]*={3}") == true) {
            return false;
        }
        if (text.matches("^={4}[^=]*={4}") == true) {
            return false;
        }
        if (text.matches("^={5}[^=]*={5}") == true) {
            return false;
        }
        if (text.matches("^={6}[^=]*={6}") == true) {
            return false;
        }
        if (RegEx.find(Patterns.DEFINITION_LIST, text) == true) {
            return false;
        }
        if (RegEx.find(Patterns.INDENT, text) == true) {
            return false;
        }
        if (RegEx.find(Patterns.ORDERED_LIST, text) == true) {
            return false;
        }
        if (RegEx.find(Patterns.BULLET, text) == true) {
            return false;
        }
        if (RegEx.find(Patterns.LINE, text) == true) {
            return false;
        }
        if (text.isEmpty() == true) {
            return false;
        }
        return true;
    }

    /**
     * @param str the substring to search for.
     * @param fromIndex the index to start the search from.
     * @return the index of the last occurrence of the specified substring,
     * searching backward from the specified index, or {@code -1} if there is no
     * such occurrence.
     */
    protected static int findClosingBracket(String str, int fromIndex) {
        int count = 0;
        int i = fromIndex;
        do {
            i++;
            if (i >= str.length()) {
                return -1;
            }
            switch (str.charAt(i)) {
                case ']':
                    count++;
                    break;
                case '[':
                    count--;
                    break;
            }
        } while (count != 0);

        return i;
    }
}
