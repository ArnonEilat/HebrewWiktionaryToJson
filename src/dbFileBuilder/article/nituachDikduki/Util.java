package dbFileBuilder.article.nituachDikduki;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Static utility class to be used by {@link NituachDikduki} class.
 */
public final class Util {

    private static Pattern pattern;
    private static Matcher matcher;

    private Util() {// Don't let anyone instantiate this class.
    }
    /**
     * Look for: <tt>'''</tt>
     */
    protected static final Pattern WIKI_BOLD = Pattern.compile("'\\'\\'", Pattern.UNICODE_CASE);
    /**
     * Pattern to detect links. i.e: <tt>[[link text]]</tt>
     *
     * @see
     * <a href="http://meta.wikimedia.org/wiki/Help:Editing#Links">Editing
     * MediaWiki - Links</a>
     */
    protected static final Pattern LINK_PATTERN = Pattern.compile("\\[\\[([^\\[\\]])*\\]\\]", Pattern.UNICODE_CASE);

    /**
     * Look for:
     * <pre>'''someText'''</pre> Covert it to
     * <pre>{@literal <}b{@literal >}someText{@literal <}b{@literal />}</pre>
     *
     * @param text to convert.
     * @return Same as text but with bold tag instead of apostrophes.
     */
    protected static String apostrophesToBoldTag(String text) {
        pattern = Util.WIKI_BOLD;
        do {
            matcher = pattern.matcher(text);
            text = matcher.replaceFirst("<b>");
            matcher = pattern.matcher(text);
            text = matcher.replaceFirst("</b>");
        } while (text.contains("'''"));
        return text;
    }

    /**
     * Returns the index within {@code str} of the first occurrence of the
     * specified {@code chars}, starting at the specified index.
     *
     * @param chars to look for.
     * @param str The string to search in.
     * @param fromIndex The index from which to start the search.
     * @return the index of the first occurrence of the specified substring,
     * starting at the specified index, or {@code -1} if there is no such
     * occurrence.
     */
    protected static int indexOf(final char[] chars, final String str, final int fromIndex) {
        for (char ch : chars) {
            if (str.indexOf(ch, fromIndex) != -1) {
                return str.indexOf(ch, fromIndex);
            }
        }
        return -1;
    }

    /**
     * Remove leading and trailing brackets from a <code>text</code>.
     *
     * @param kind of bracket to remove can be: '{' , '[' or '('.
     * @param text to remove bracket from.
     * @return The same as <code>text</code> but without bracket.
     */
    protected static String trimBracket(final String text, final char kind) {
        int left = 0;
        int right = text.length() - 1;
        char close = '}';
        switch (kind) {
            case '[':
                close = ']';
                break;
            case '(':
                close = ')';
                break;
        }
        while (text.charAt(left) == kind) {
            left++;
        }
        while (text.charAt(right) == close) {
            right--;
        }
        right++;
        return text.substring(left, right);
    }

    /**
     * Remove punctuation marks, extra spaces and line feeds from <tt>text</tt>.
     *
     * @param dirty String to convert to plain text.
     * @return The same as <tt>dirty</tt> but clean.
     */
    protected static String toPlain(String dirty) {
        dirty = dirty.replaceAll("\\[\\[[^\\|]\\|", "");
        dirty = dirty.replaceAll("\\]\\]", "");
        dirty = dirty.replaceAll("'''", "");
        dirty = dirty.replaceAll("''", "");
        dirty = dirty.replaceAll("<BR>|<br>", " ");
        dirty = dirty.replaceAll("\n", " ");
        dirty = dirty.replaceAll("  ", " ");

        return dirty.trim();
    }

    /**
     * Convert bold(''') and italics('') wiki markup to HTML.
     *
     * @param wiki text to convert to HTML.
     * @return The same as <tt>wiki</tt> but with HTML formatting.
     */
    protected static String toHtml(String wiki) {
        wiki = wiki.replaceAll("<BR>|<br>", " ");
        wiki = wiki.replaceAll("\n", " ");
        wiki = wiki.replaceAll("  ", " ");

        int countBold = 0;
        int indexOfApostrophe = wiki.indexOf("'''");
        while (indexOfApostrophe != -1) {
            if ((countBold % 2) == 0) {
                wiki = wiki.replaceFirst("'''", "<b>");
            } else {
                wiki = wiki.replaceFirst("'''", "</b>");
            }
            countBold++;
            indexOfApostrophe = wiki.indexOf("'''", indexOfApostrophe);
        }
        int countItalic = 0;
        indexOfApostrophe = wiki.indexOf("''");
        while (indexOfApostrophe > -1) {
            if ((countItalic % 2) == 0) {
                wiki = wiki.replaceFirst("''", "<i>");
            } else {
                wiki = wiki.replaceFirst("''", "</i>");
            }
            countItalic++;
            indexOfApostrophe = wiki.indexOf("''", indexOfApostrophe);
        }
        wiki = wiki.replace("<\\/b><\\/i>", "</i></b>");
        return wiki.trim();
    }

    /**
     * Count the number of appearance of <tt>toCount</tt> in <tt>longOne</tt>.
     *
     * @param longOne String to search on.
     * @param toCount String to count.
     * @return Number of appearance of <tt>toCount</tt> in <tt>longOne</tt>.
     */
    protected static int count(String longOne, String toCount) {
        int i = 0;

        while (longOne.contains(toCount)) {
            i++;
            longOne = longOne.replace(toCount, "");
        }
        return i;
    }

    /**
     * Returns 2 characters length string at the specified <tt>index</tt>.
     *
     * @param str String to retrieve from.
     * @param index the index of the string value.
     * @return The String at the specified index of <tt>str</tt>.
     */
    protected static String stringAt(String str, int index) {
        return str.substring(index, index + 2);
    }
}
