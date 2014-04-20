package dbFileBuilder.wiki;

import java.util.regex.Pattern;

/**
 * Class to deal with list.
 */
public final class ListUtil {

    /**
     * Used by {@link  ListUtil#removeFirstListSign removeFirstListSign} method.
     */
    private static final Pattern LIST_SIGN_REMOVER
            = Pattern.compile("(^|\\n)[\\*#:;]{0,1}", Pattern.UNICODE_CASE);

    /**
     * Don't let anyone instantiate this class.
     */
    private ListUtil() {
    }

    /**
     * @param text to check whether it's list or not.
     * @return <tt>true</tt> if and only if <tt>str</tt> containing a list on
     * it.
     */
    protected static boolean isAList(String text) {
        String arr[] = Util.splitByNewLineSign(text);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].length() != 0) {
                if (isAListChar(arr[i].charAt(0)) == true) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns string with <tt>HTML</tt> list tag corresponding to the first
     * character of<tt>str</tt>.
     *
     * @param str
     * @return Return values specified on the flowing table.
     * <br>
     * <table style="width: 40%" border="1">
     * <tbody>
     * <tr>
     * <td>First character of <tt>str</tt></td>
     * <td>Returned value</td>
     * <td>HTML Meaning</td>
     * </tr>
     * <tr>
     * <td>#</td>
     * <td>ol</td>
     * <td>Ordered list</td>
     * </tr>
     * <tr>
     * <td>{@literal *}
     * </td>
     * <td>ul</td>
     * <td>Unordered list</td>
     * </tr>
     * <tr>
     * <td>:</td>
     * <td>dl</td>
     * <td>Term<br>
     * </td>
     * </tr>
     * </tbody>
     * </table>
     */
    protected static String getListTagForString(String str) {
        char chr = str.charAt(0);
        switch (chr) {
            case '#':
                return "ol";
            case '*':
                return "ul";
            case ':':
            case ';':
                return "dl";
        }
        return null;
    }

    /**
     * Return String inner list tag according to <tt>listType</tt>.
     *
     * @param listType Acceptable values specified on the flowing table in the
     * "<i>Gets</i>" column.
     * @return String with <tt>HTML</tt> tag as specified on the flowing table
     * in the "<i>Return</i>" column.
     * <table style="width: 30%" border="1">
     * <tbody>
     * <tr>
     * <td>Gets</td>
     * <td>Return</td>
     * <td>Meaning</td>
     * </tr>
     * <tr>
     * <td>ul</td>
     * <td colspan="1" rowspan="2">li</td>
     * <td>Ordered list</td>
     * </tr>
     * <tr>
     * <td>ol</td>
     * <td>Unordered list</td>
     * </tr>
     * <tr>
     * <td>dl</td>
     * <td>dd</td>
     * <td>Term</td>
     * </tr>
     * </tbody>
     * </table>
     * @see <a href="http://meta.wikimedia.org/wiki/Help:List">MediaWiki
     * Help:List</a>
     */
    protected static String getInnerListTag(String listType) {
        if (listType.equals("dl")) {
            return "dd";
        }
        if (listType.equals("ul") || listType.equals("ol")) {
            return "li";
        }
        return null;

    }

    /**
     * Remove first list sign (can be #, * Or :) from each line on
     * <tt>text</tt>.
     *
     * @param text to remove first list sign from its beginning.
     * @return Same as {@code text } but without first list sign.
     */
    protected static String removeFirstListSign(String text) {
        return LIST_SIGN_REMOVER.matcher(text).replaceAll("\n");
        //<editor-fold defaultstate="collapsed" desc="Another way to impliment">
        // Another way to impliment:
        // return str.replaceAll("(^|\\n)[\\*#:]{0,1}", "\n");
        /*
         * // Another way to impliment... String[] arr =
         * splitByNewLineSign(str); String strToRet = ""; for (int i = 0; i <
         * arr.length; i++) { if (isAListChar(arr[i].charAt(0))) strToRet +=
         * arr[i].substring(1, arr[i].length()) + '\n'; else strToRet += arr[i]
         * + '\n'; } return removeNewLineSignFromEnd(strToRet);
         */
        //</editor-fold>
    }

    /**
     * Returns <tt>true</tt> if and only if <tt>character</tt> is one of the
     * list chars.<br>
     * The list chars are:<ul>
     * <li><tt>#</tt> Represent unordered list</li>
     * <li><tt>{@literal *}</tt> Represent ordered list</li>
     * <li><tt>:</tt> Represent definition list</li></ul>
     *
     * @see <a href="http://meta.wikimedia.org/wiki/Help:List">MediaWiki
     * Help:List</a>
     * @return <tt>true</tt> if and only if <tt>character</tt> is a list char.
     * @param character to check if it list char.
     */
    protected static boolean isAListChar(char character) {
        switch (character) {
            case ':':
            case ';':
            case '#':
            case '*':
                return true;
        }
        return false;
    }
}
