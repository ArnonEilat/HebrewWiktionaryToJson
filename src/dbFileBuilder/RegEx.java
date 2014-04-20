package dbFileBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class with helpful regular expression methods.
 */
public final class RegEx {
    /*
     * Don't let anyone instantiate this class.
     */

    private RegEx() {
    }

    /**
     * Attempts to find the subsequence of the input sequence that matches the
     * <tt>pattern</tt>.
     *
     * @param pattern The expression to be compiled
     * @param input The character sequence to be matched
     * @return <tt>true</tt> if, and only if, a subsequence of the input
     * sequence matches the <tt>pattern</tt>.
     */
    public static boolean find(Pattern pattern, String input) {
        Pattern p = pattern;
        Matcher m = p.matcher(input);
        return m.find();
    }

    /**
     * Remove all occurrence of pattern from <tt>input</tt>.
     *
     * @param input to remove all occurrence from.
     * @param pattern to look for.
     * @return The same as <tt>input</tt> but without occurrence of
     * <tt>pattern</tt>.
     */
    public static String removeAll(String input, Pattern pattern) {
        return replaceAll(input, pattern, "");
    }

    /**
     * Replaces each substring of this string that matches the given pattern
     * with the given replacement.
     *
     * @param input to replace all occurrence from
     * @param pattern the regular expression to which this string is to be
     * matched
     * @param replacement the string to be substituted for each match
     * @return The resulting <tt>String</tt>
     */
    public static String replaceAll(String input, Pattern pattern, String replacement) {
        Pattern p = pattern;
        Matcher m = p.matcher(input);
        return m.replaceAll(replacement);
    }
}
