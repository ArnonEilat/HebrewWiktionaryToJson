package dbFileBuilder.article;

import dbFileBuilder.utilities.ArrayUtils;
import dbFileBuilder.RegEx;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b>NSWMP</b> stands for <b>N</b>o <b>S</b>tructured <b>W</b>iki <b>M</b>arkup
 * <b>P</b>rocessor.<br>
 * Because the {@link dbFileBuilder.wiki.Wiki} library does not support tables
 * and we don't want to dirty the output, we preprocessing the input in a non
 * structured way.
 */
public final class NSWMP {

    /**
     * @see NSWMP#isStringTooIndent(java.lang.String)
     */
    private static final Pattern tooIndent = Pattern.compile("(^#|^\\*)(:{1,})([^:])", Pattern.UNICODE_CASE);
    /**
     * Constant indicate open table prefix.<br>
     * Constant Value : {@value}.
     */
    private static final String OPEN_TABLE_PRFX = "{{";
    /**
     * Constant indicate close table prefix.<br>
     * Constant Value : {@value}.
     */
    private static final String CLOSE_TABLE_PRFX = "}}";

    /**
     * Converts all the tables in <tt>wikiText</tt> to non table format.
     *
     * @param wikiText to converts all its tables to non table format.
     * @return The same text as <tt>wikiText</tt> but with tables as a normal
     * text.
     */
    protected static String flattenTable(final String wikiText) {
        String[] linesArr = wikiText.split("\\n");
        if (linesArr.length == 1) {
            return wikiText;
        }
        boolean insideTable = false;
        StringBuilder output = new StringBuilder("");
        int toggelLine = -1;

        for (int i = 0; i < linesArr.length; i++) {
            int match = countMatches(linesArr[i], OPEN_TABLE_PRFX);
            match -= // Compare between  open bracket to close bracket
                    countMatches(linesArr[i], CLOSE_TABLE_PRFX);

            if (match == -1 && insideTable == true) {
                insideTable = false;
                toggelLine = i;
            }
            if (match == 1 && insideTable == false) {
                insideTable = true;
            }

            if (linesArr[i].equals("|")) // Omit new row sign - its dirt
            {
                continue;
            }

            if (insideTable == false) {
                if (toggelLine == i) {
                    linesArr[i] = lineFlow(linesArr[i], true, true);
                } else {
                    linesArr[i] = lineFlow(linesArr[i], true, false);
                }
            } else {
                if (toggelLine == i) {
                    linesArr[i] = lineFlow(linesArr[i], false, true);
                } else {
                    linesArr[i] = lineFlow(linesArr[i], false, false);
                }
            }
            output.append(linesArr[i]).append('\n');
        }
        return output.toString();
    }

    /**
     * An flow (flow like a flowchart) to deal with one line.
     *
     * @param line to process
     * @param insideTable <tt>true</tt> if the <tt>line</tt> is inside a table,
     * otherwise <tt>false</tt>.
     * @param isALastLine <tt>true</tt> if the <tt>line</tt> is the last line of
     * the table, otherwise <tt>false</tt> .
     */
    private static String lineFlow(String line, boolean insideTable, boolean isALastLine) {
        // TODO : documentation
        if (insideTable == true) {
            if (line.startsWith("|")) {
                line = line.substring(1, line.length());
            }
            if (line.endsWith("|")) {
                line = line.substring(0, line.length() - 1);
            }
            if (line.startsWith("{{עמודות")) {
                return "";
            }
            if (line.startsWith("{{קצרמר}}")) {
                return "{{קצרמר}}";
            }
            if (line.startsWith("{{")) {
                //  return line.substring(2, line.length());
            }
        }
        if (isALastLine == true) {
            if (line.endsWith("}}")) {
                return line.substring(0, line.length() - 2);
            }
        } else {
            if (line.startsWith("{{")) {
                //     return line.substring(2, line.length());
            }
        }
        return line;
    }

    /**
     * Counts how many times the substring appears in the larger string.<br>
     * A {@code null} or empty ("") String input returns {@code 0}.
     * <blockquote>
     * <pre>
     * NSWMP.countMatches(null, *)       = 0
     * NSWMP.countMatches("", *)         = 0
     * NSWMP.countMatches("abba", null)  = 0
     * NSWMP.countMatches("abba", "")    = 0
     * NSWMP.countMatches("abba", "a")   = 2
     * NSWMP.countMatches("abba", "ab")  = 1
     * NSWMP.countMatches("abba", "xxx") = 0
     * </pre>
     * </blockquote>
     *
     * @param bigString the CharSequence to check, may be null
     * @param substring the substring to count, may be null
     * @return The number of occurrences, 0 if either substring is not in
     * bigString
     */
    private static int countMatches(final String bigString, final String substring) {
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {
            lastIndex = bigString.indexOf(substring, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += substring.length();
            }
        }
        return count;
    }

    /**
     * Remove from <tt>wikiText</tt>.<br>
     * {@link #isStringTooIndent(String) isStringTooIndent}
     *
     * @see dbFileBuilder.article.NSWMP#isStringTooIndent(java.lang.String)
     * @param wikiText to remove unnecessary indent sign from
     * @return The same text as <tt>wikiText</tt> but without unnecessary indent
     * sign.
     */
    protected static String flattenIndent(final String wikiText) {
        String[] linesArr = wikiText.split("\\n");
        for (int i = 0; i < linesArr.length; i++) {
            if (isStringTooIndent(linesArr[i])) {
                linesArr[i] = linesArr[i].replaceFirst(":{1,}", "");
            }
        }
        return ArrayUtils.join(linesArr, "\n");
    }

    /**
     * Returns true if <tt>text</tt> is too indent.<br>
     * Too indent text is text with unnecessary indent sign.<br>
     * <blockquote>
     * <tt> {@literal *} a list item...<br> {@literal *} Another list item<br>
     * {@literal **} Some more, but in<br> {@literal *:*} This one is too indent
     * </tt>
     *
     * </blockquote>
     *
     * @param text to check whether its too indent or not.
     */
    private static boolean isStringTooIndent(final String text) {
        Pattern p = tooIndent;
        Matcher m = p.matcher(text);
        return m.find();
    }

    /**
     * Convert To Wiki Indent looks for numbers at the start of the line
     *
     * @param wikiText
     * @return TODO: complete
     */
    protected static String convertToWikiIndent(String wikiText) {
        String[] linesArr = wikiText.split("\\n");

        for (int i = 0; i < linesArr.length; i++) {
            if (linesArr[i].trim().isEmpty() == false) {
                char firstChar = linesArr[i].trim().charAt(0);
                if (Character.isDigit(firstChar)) {
                    linesArr[i] = "#" + linesArr[i].trim().substring(2);
                }
            }
        }
        return ArrayUtils.join(linesArr, "\n");
    }

    /**
     * Check whether wikiText is a good entry or not.
     *
     * @param wikiText of the entry.
     * @param title of the entry.
     * @return <tt>true</tt> if wikiText is good entry, otherwise
     * <tt>false</tt>.
     */
    protected static boolean isAGoodEntry(String wikiText, String title) {
        if (title.contains("(שורש)") == true) {// Shoresh pages
            return false;
        }

        if (wikiText.contains("מה ההבדל בין רצ\"ב למצ\"ב?") == true) {
            return false;
        }
        if (title.contains("ניטים") == true) {
            return false;
        }

        if (wikiText.contains("{{שהות}}") == true) {
            //int lines = StringUtility.countOccurrences(wikiText, '\n');
            return false;
        }

        if (RegEx.find(Patterns.ARAMAIC_FIX, wikiText) == true) {
            return false;
        }
        if (RegEx.find(Patterns.GREEK_FIX, wikiText) == true) {
            return false;
        }

        if (title.length() <= 1) {
            /**
             * Each hebrew alphabet letter get its one entry. as well as some of
             * the English, Chinese/Japanese pictogram..
             *
             * The processing overhead is greater than the outcome.
             */
            return false;
        }
        if (wikiText.contains("==אֲחָד==") == true) {
            return false; // We do not want these value // in othere release it  it could be a good entry
        }

        if (dbFileBuilder.RegEx.find(Patterns.GERMAN, wikiText) == true) {
            return false; // We do not want these values
        }
        if (dbFileBuilder.RegEx.find(Patterns.GERMANIT, wikiText) == true) {
            return false; // We do not want these values
        }
        if (wikiText.contains("== אנגלית: Berg ==") == true) {
            return false; // We do not want these values
        }
        if (wikiText.contains("==איסלנדית==") == true) {
            return false; // We do not want these values
        }
        if (wikiText.contains("עבירה") == true) {
//            return false; // We do not want these values
        }
        if (title.contains("אוקריוטי") == true) {
            return false; // We do not want these values
        }

        return true;
    }

    /**
     * Remove images from wikiText - don't need them.
     *
     * @param wikiText
     * @return text without images.
     */
    protected static String preprocessor(String wikiText) {

        if (RegEx.find(Patterns.IMAGE_OTHER_VARIANT, wikiText) == true) {
            wikiText = RegEx.removeAll(wikiText, Patterns.IMAGE_OTHER_VARIANT);
        }

        if (RegEx.find(Patterns.IMAGE_CURLY, wikiText) == true) {
            wikiText = RegEx.removeAll(wikiText, Patterns.IMAGE_CURLY);
        }

        wikiText = RegEx.removeAll(wikiText, Patterns.IMAGE);

        return wikiText;
    }
}
