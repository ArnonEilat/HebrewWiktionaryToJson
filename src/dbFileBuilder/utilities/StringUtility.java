package dbFileBuilder.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class with useful functions dealing with strings.
 */
public final class StringUtility {
    private static Pattern pattern;
    private static Matcher matcher;
    /**
     * Pattern to detect open page tag.<br>
     * e.i: <tt>{@literal <}page{@literal >}</tt>
     */
    public static final Pattern OPEN_PAGE_TAG = Pattern.compile("\\s*<page>\\s*", Pattern.UNICODE_CASE);
    /**
     * Pattern to detect close page tag.<br>
     * e.i: <tt>{@literal <}/page{@literal >}</tt>
     */
    public static final Pattern CLOSE_PAGE_TAG = Pattern.compile("\\s*</page>\\s*", Pattern.UNICODE_CASE);

    /**
     * Return {@code true} only if {@code str} is an open/close page XML tag.
     *
     * @param str   String to check whether its tag or not.
     * @param state <tt>True</tt> to look for open page tag
     *              (<tt>{@literal <}page{@literal >}</tt>) , <code>false</code>
     *              to look for close page
     *              tag(<tt>{@literal <}/page{@literal >}</tt>).
     * @return {@code true} if, and only if {@code str} is an open or close XML
     *         page tag.
     */
    public static boolean isAPageTag(String str, boolean state) {
        if (state == true) {
            StringUtility.pattern = OPEN_PAGE_TAG;
        } else {
            StringUtility.pattern = CLOSE_PAGE_TAG;
        }

        StringUtility.matcher = pattern.matcher(str);
        return StringUtility.matcher.matches();
    }

    /**
     * Remove all HTML tags and their contents from {@code str}.<br>
     * Example :
     * <blockquote>
     * <pre>"some string{@literal <}strong{@literal >}Text in
     * Tag{@literal <}strong{@literal />}"</pre>
     * </blockquote>
     * Will by :
     * <blockquote>
     * <pre>"some string"</pre>
     * </blockquote>
     * @param text to remove all HTML tags and their contents from.
     * @return String without HTML tags.
     */
    public static String removeHTML(String text) {
        return text.replaceAll("<[^>]*>(.*?)</[^>]*>", "");
    }

    /**
     * Count occurrences of <tt>c</tt> in haystack.
     * @param needle   to find in <tt>haystack</tt>.
     * @param haystack to find <tt>needle</tt> in.
     * @return Number of times <tt>needle</tt> appears in <tt>haystack</tt>.
     */
    public static int countOccurrences(final String haystack, final char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }

    /**
     * Remove all the<tt>\n</tt> (new line sign) from <tt>text</tt>.
     * @param text to remove newline sign from.
     * @return same String as <tt>text</tt> but without newline sings.
     */
    public static String removeNewLine(String text) {
        do {
            text = text.replaceAll("\n", "");
        } while (text.contains("\n"));
        return text;
    }
}

