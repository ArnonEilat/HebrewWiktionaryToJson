package dbFileBuilder.wiki;

import dbFileBuilder.RegEx;
import dbFileBuilder.utilities.ArrayUtils;
import java.util.regex.Pattern;

/**
 * Class with methods to process small texts.
 */
public final class Processors {

    /**
     * Define the Origin of the Quote.
     */
    private enum QuoteOrigin {

        /**
         * אונקלוס
         *
         * @see
         * <a
         * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%90%D7%95%D7%A0%D7%A7%D7%9C%D7%95%D7%A1"
         * >צט/אונקלוס</a>
         */
        ONKELOS,
        /**
         * תלמוד בבלי
         *
         * @see <a
         * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%91%D7%91%D7%9C%D7%99"
         * >צט/בבלי</a>
         */
        BAVLI,
        /**
         * תלמוד ירושלמי
         *
         * @see <a
         * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%99%D7%A8%D7%95%D7%A9%D7%9C%D7%9E%D7%99"
         * >צט/ירושלמי</a>
         */
        YERUSHALMI,
        /**
         * תלמוד ירושלמי הלכה
         *
         * @see
         * <a
         * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%99%D7%A8%D7%95%D7%A9%D7%9C%D7%9E%D7%99_%D7%94%D7%9C%D7%9B%D7%94"
         * >צט/ירושלמי הלכה</a>
         */
        YERUSHALMI_HALACHA,
        /**
         * מדרש רבה
         *
         * @see <a
         * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%A8%D7%91%D7%94"
         * >צט/רבה</a>
         */
        RABBAH,
        /**
         * תנ"ך
         *
         * @see
         * <a
         * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%AA%D7%A0%22%D7%9A"
         * >צט/תנ"ך</a>
         */
        TANAKH,
        /**
         * ללא מרכאות
         *
         * @see <a
         * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%9C%D7%9C%D7%90"
         * >צט/ללא</a>
         */
        WITHOUT,
        /**
         * No origin mentioned
         *
         * @see <a
         * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98">תבנית:צט</a>
         */
        EMPTY
    }

    private static final Pattern SHORT_QUOTE_PATTERN = Pattern.compile("\\Aצט[^\\|]*\\|", Pattern.UNICODE_CASE);
    private static final char QUOTATION_MARK = '"';
    private static final String SPAN_QUOTE = "<span class=\"quote\">\"";
    private static final String SPAN_QUOTE_ORIGIN = " <span class=\"quote-origin\">(";
    private static final String SPAN_CLOSE = "</span>";
    private static final Pattern SMALL_QUOTE_PATTERN = Pattern.compile("\\Aציטוטון[^\\|]*\\|", Pattern.UNICODE_CASE);

    /**
     * Don't let anyone instantiate this class.
     */
    private Processors() {
    }

    /**
     * @see <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%9E%D7%A9%D7%9C%D7%91">תבנית:משלב</a>
     * @param text to process.
     */
    protected static String processRegister(String text) {
        StringBuilder output = new StringBuilder("");

        text = RegEx.removeAll(text, Patterns.REGISTER);

        String[] arr = text.split("\\|");
        output.append("<span class=\"linguisticRegister\">[");
        for (String string : arr) {
            string = string.trim();
        }

        output.append(ArrayUtils.join(arr, ", "));
        output.append("]</span>");

        return output.toString();
    }

    /**
     * @param text to process.
     * @see <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A8%D7%95%D7%91%D7%93">תבנית:רובד</a>
     *
     */
    protected static String processStratum(String text) {
        text = text.replace("רובד|", "");
        return "<span class=\"rovedLashon\">" + text.trim() + "</span>";
    }

    /**
     *
     * @param text to process.
     * @see <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%97%D7%99%D7%93%D7%95%D7%A9">תבנית:חידוש</a>
     */
    protected static String processNewHebrew(String text) {
        String arr[] = text.split("\\|");

        String html = "<span class=\"hidush\">חידוש של " + arr[1] + ", " + arr[2].replace("שנה=", "") + ".</span>";
        return html;
    }

    /**
     * @param text to process.
     * @see <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%9C%D7%95%D7%A2%D7%96%D7%99%D7%AA">תבנית:לועזית</a>
     */
    protected static String processForeignLanguage(String text) {
        text = text.replace("לועזית|", "מ");

        return text + ": ";
    }

    /**
     * Process Greek pattern.<br>
     * Add <tt>data-transliterationLanguage="grc"</tt> attribute to the
     * transliteration(if exist), and <tt>lang="grc"</tt> attribute to the Greek
     * word itself.<br>
     * <b>Note</b>:<br>
     * The language code of ancient Greek is <tt>grc</tt>, NOT
     * <tt>el</tt>(Modern Greek)
     *
     * @param text to process.
     * @see <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%99%D7%95%D7%95%D7%A0%D7%99%D7%AA">תבנית:יוונית</a>
     */
    protected static String processGreek(String text) {
        text = text.replace("יוונית|", "");
        String arr[] = text.split("\\|");
        if (arr.length >= 2) {
            text = "<span data-transliterationLanguage=\"grc\">"
                    + arr[1] + "</span>) <span lang=\"grc\">"
                    + arr[0] + "</span>)";
        } else {
            text = "<span lang=\"grc\">" + text + "</span>";
        }
        return text;
    }

    /**
     * @param text to process.
     * @see <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%99%D7%95%D7%A0%D7%99%D7%A7%D7%95%D7%93">תבנית:יוניקוד</a>
     */
    protected static String processUnicode(String text) {
        boolean big = false;
        text = text.replace("|הגדלה=ללא", "");
        text = text.replace("|הגדלה=לא", "");
        text = text.replace("יוניקוד|", "");
        if (text.contains("הגדלה")) {
            big = true;
        }
        if (text.contains("מוגדל")) {
            big = true;
        }
        text = text.replace("|מוגדל", "");
        text = text.replace("|הגדלה", "");
        if (big == true) {
            text = "<span class=\"unicode unicode-big\">" + text + "</span>";
        } else {
            text = "<span class=\"unicode\">" + text + "</span>";
        }

        return text;
    }

    /**
     * @param text to process.
     * @see <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A8%22%D7%AA">תבנית:ר"ת</a>
     */
    protected static String processAbbreviation(String text) {
        StringBuilder output = new StringBuilder("");
        String arr[] = text.split("\\|");

        output.append(arr[2]);
        output.append(" (");
        output.append(arr[1]);
        output.append(") ");
        if (arr.length >= 4) {
            output.append("<br>");
            output.append(arr[3]);
        }

        return output.toString();
    }

    /**
     * Gets string with URL and text(optional) separated with space and convert
     * it to HTML a tag.<br>
     * E.g:
     * <blockquote><pre>
     *  http://www.google.com Google
     * will be:
     * {@literal <}a href="http://www.google.com">Google{@literal <}/a>
     * </pre></blockquote>
     *
     * @param txt Wiki markup image
     */
    protected static String processURL(String txt) {
        int index = txt.indexOf(" ");
        String url = txt;
        String label = txt;
        if (index != -1) {
            url = txt.substring(0, index);
            label = txt.substring(index + 1);
        }
        return "<a href=\"" + url + "\">" + label + "</a>";
    }

    /**
     * Gets string with URL to image and alternate text attribute(optional)
     * separated with vertical bar (|) and convert it to HTML image tag.<br>
     * E.i:
     * <blockquote><pre>
     *[[File:path/to/image/file.jpg|alt=Alternative Text]]
     * </blockquote></pre> will be:
     * <blockquote><pre>
     * {@literal <}img src="path/to/image/file.jpg" alt="Alternative Text"/>";
     * </blockquote></pre>
     *
     * @param txt Wiki markup image
     */
    protected static String processImage(String txt) {
        /*
         * String url; String label = ""; String[] arr = txt.split("\\|"); int
         * lastIndex = arr.length - 1; url = arr[0] + "\""; if (lastIndex != 1)
         * { arr[lastIndex] = arr[lastIndex].replaceAll("\\[", "");
         * arr[lastIndex] = arr[lastIndex].replaceAll("]", ""); label = "
         * alt=\"" + arr[lastIndex] + "\" "; } return "<img src=\"" + url +
         * label + " />";
         */
        return ""; /*
         * Images not needed.
         */

    }

    /**
     * Gets a line of Hdgasha entry and convert it to appropriate HTML
     * <tt>span</tt> tag.<br>
     * The <tt>text</tt> parameter should be the form like:
     * <blockquote><tt>
     * הדגשה|complete sentence|reference
     * </tt></blockquote>
     *
     * @return String with two <tt>span</tt> tags :
     * <blockquote>
     * The first containing the <i>sentence</i>, and <tt>class</tt> attribute
     * with "<tt>emphasis</tt>" value attached to it.<br>
     * The second containing the <i>reference</i>, and <tt>class</tt> attribute
     * with "<tt>afterEmphasis</tt>" value attached to it.
     * </blockquote>
     * @param text to process.
     * @see <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%94%D7%93%D7%92%D7%A9%D7%94">תבנית:הדגשה</a>
     */
    protected static String processEmphasis(String text) {
        String[] arr = text.split("\\|");
        if (arr.length == 2) {
            return "<span class=\"emphasis\">" + arr[1] + "</span>";
        }
        return "<span class=\"emphasis\">" + arr[1] + "</span><span class=\"afterEmphasis\">" + arr[2] + "</span>";
    }

    /**
     * Gets a line of Inner link entry and convert it to appropriate HTML
     * <tt>a href</tt> tag.<br>
     * The <tt>text</tt> parameter should be the form like:
     * <blockquote><tt>
     * value to link to|displayable text</tt><br>
     * Or:<br>
     * <tt>text</tt>
     * </blockquote>
     *
     * @return String with HTML <tt>a</tt> tag containing the displayable
     * text.<br>
     * The value of <tt>href</tt> attribute is <tt>innerURL://value to link
     * to</tt>.
     * @param text to process.
     */
    protected static String processInnerURL(String text) {
        int index = text.indexOf("|");
        String url = text;
        String label = text;
        if (index != -1) {
            url = text.substring(0, index);
            label = text.substring(index + 1);
        }
        return "<a href=\"innerURL://" + url + "\">" + label + "</a>";
    }

    /**
     * //TODO: impliment.
     *
     * @see
     * <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%99%D7%98%D7%95%D7%98">תבנית:ציטוט</a>
     */
    protected static String processQuote(String text) {

        //  System.out.println(text);
        return text;
    }

    /**
     * @param text to process.
     * @see
     * <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%99%D7%98%D7%95%D7%98%D7%95%D7%9F">תבנית:ציטוטון</a>
     */
    protected static String processSmallQuote(String text) {
        text = RegEx.removeAll(text, SMALL_QUOTE_PATTERN);
        String[] arr = text.split("\\|");
        StringBuilder sb = new StringBuilder(100);
        sb.append("<span class=\"quote-small\">");
        sb.append(arr[0]);
        sb.append(SPAN_CLOSE);
        if (arr.length > 1) {
            sb.append(", ");
            sb.append(arr[1]);
        }

        return sb.toString();
    }

    private static QuoteOrigin getOrigin(String text) {
        if (text.contains("/אונקלוס")) {
            return QuoteOrigin.ONKELOS;
        }
        if (text.contains("/בבלי")) {
            return QuoteOrigin.BAVLI;
        }
        if (text.contains("/ירושלמי")) {
            return QuoteOrigin.YERUSHALMI;
        }
        if (text.contains("/ירושלמי הלכה")) {
            return QuoteOrigin.YERUSHALMI_HALACHA;
        }
        if (text.contains("/רבה")) {
            return QuoteOrigin.RABBAH;
        }
        if (text.contains("/תנ\"ך")) {
            return QuoteOrigin.TANAKH;
        }
        if (text.contains("/ללא")) {
            return QuoteOrigin.WITHOUT;
        }

        return QuoteOrigin.EMPTY;
    }

    private static String addQuotationMark(String text) {
        char[] arr = text.toCharArray();

        if (arr.length < 2) {
            return text;
        }
        StringBuilder sb = new StringBuilder(3);
        sb.append(arr[0]);
        sb.append('"');
        sb.append(arr[1]);

        return sb.toString();
    }

    /**
     * @param text to process.
     * @see
     * <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%90%D7%95%D7%A0%D7%A7%D7%9C%D7%95%D7%A1"
     * >צט/אונקלוס</a><br>
     * <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%91%D7%91%D7%9C%D7%99"
     * >צט/בבלי</a><br>
     * <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%99%D7%A8%D7%95%D7%A9%D7%9C%D7%9E%D7%99"
     * >צט/ירושלמי</a><br>
     * <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%99%D7%A8%D7%95%D7%A9%D7%9C%D7%9E%D7%99_%D7%94%D7%9C%D7%9B%D7%94"
     * >צט/ירושלמי הלכה</a><br>
     * <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%A8%D7%91%D7%94"
     * >צט/רבה</a><br>
     * <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%AA%D7%A0%22%D7%9A"
     * >צט/תנ"ך</a><br>
     * <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A6%D7%98/%D7%9C%D7%9C%D7%90"
     * >צט/ללא</a><br>
     */
    protected static String processShortQuote(String text) {
        StringBuilder output = new StringBuilder(400);
        QuoteOrigin Origin = Processors.getOrigin(text);
        text = RegEx.removeAll(text, SHORT_QUOTE_PATTERN);
        String[] arr;

        arr = text.split("\\|");
        switch (Origin) {
            case ONKELOS:
                output.append(SPAN_QUOTE);
                output.append(arr[0]);
                output.append(QUOTATION_MARK);
                output.append(SPAN_CLOSE);
                output.append(SPAN_QUOTE_ORIGIN);
                output.append(" (אונקלוס על ");
                output.append(arr[1]);
                output.append(" ");
                output.append(arr[2]);
                output.append(" – פסוק ");
                output.append(arr[3]);
                output.append('(');
                output.append(SPAN_CLOSE);
                break;
            case BAVLI:
                output.append(SPAN_QUOTE);
                output.append(arr[0]);
                output.append(QUOTATION_MARK);
                output.append(SPAN_CLOSE);
                output.append(SPAN_QUOTE_ORIGIN);
                output.append("בבלי, מסכת ");
                output.append(arr[1]);
                output.append(" – דף ");
                output.append(arr[2]);
                output.append(", עמוד ");
                output.append(arr[3]);
                output.append("׳)");
                output.append(SPAN_CLOSE);
                break;
            case YERUSHALMI:
                output.append(SPAN_QUOTE);
                output.append(arr[0]);
                output.append(QUOTATION_MARK);
                output.append(SPAN_CLOSE);
                output.append(SPAN_QUOTE_ORIGIN);
                output.append("ירושלמי, מסכת ");
                output.append(arr[1]);
                output.append(" – דף ");
                output.append(arr[2]);
                output.append(", עמוד ");
                output.append(arr[3]);
                output.append("׳)");
                output.append(SPAN_CLOSE);
                break;
            case YERUSHALMI_HALACHA:
                output.append(SPAN_QUOTE);
                output.append(arr[0]);
                output.append(QUOTATION_MARK);
                output.append(SPAN_CLOSE);
                output.append(SPAN_QUOTE_ORIGIN);
                output.append("ירושלמי, מסכת ");
                output.append(arr[1]);
                output.append(" – פרק ");
                output.append(arr[2]);
                output.append("׳, הלכה ");
                output.append(arr[3]);
                output.append("׳)");
                output.append(SPAN_CLOSE);
                break;
            case RABBAH:
                output.append(SPAN_QUOTE);
                output.append(arr[0]);
                output.append(QUOTATION_MARK);
                output.append(SPAN_CLOSE);
                output.append(SPAN_QUOTE_ORIGIN);
                output.append(arr[1]);
                output.append(" רבה, פרשה ");
                output.append(arr[2]);
                output.append(", סימן ");
                output.append(arr[3]);
                output.append(')');
                output.append(SPAN_CLOSE);
                break;
            case TANAKH:
                output.append(SPAN_QUOTE);
                output.append(arr[0]);
                output.append(QUOTATION_MARK);
                output.append(SPAN_CLOSE);
                output.append(SPAN_QUOTE_ORIGIN);
                output.append(arr[1]);
                output.append(" ");
                output.append(arr[2]);
                if (arr.length == 5) {
                    output.append(" – פסוקים ");
                    output.append(addQuotationMark(arr[3]));
                    output.append('-');
                    output.append(addQuotationMark(arr[4]));
                } else {
                    output.append(" – פסוק ");
                    output.append(addQuotationMark(arr[3]));
                }
                output.append(')');
                output.append(SPAN_CLOSE);
                break;
            case WITHOUT:
                output.append(SPAN_QUOTE.substring(0, SPAN_QUOTE.length() - 1));
                output.append(arr[0]);
                output.append(SPAN_CLOSE);
                output.append(SPAN_QUOTE_ORIGIN);
                output.append(arr[1]);
                output.append(SPAN_CLOSE);
                output.append(')');
                break;
            case EMPTY:
                output.append(SPAN_QUOTE);
                output.append(arr[0]);
                output.append('"');
                output.append(SPAN_CLOSE);
                output.append(SPAN_QUOTE_ORIGIN);
                output.append(arr[1]);
                output.append(SPAN_CLOSE);
                output.append(')');
                break;

        }
        return output.toString();
    }

    /**
     * Remove the TOC (Table Of Contents) markup(and contents) from
     * <tt>text</tt>.
     *
     * @return text without TOC
     * @param text to process.
     */
    protected static String processHeading(String text) {
        String str = RegEx.removeAll(text, Patterns.TOK);
        str = str.trim();
        return str;
    }

    /**
     * Gets a line of translation entry and convert it to HTML <tt>span</tt>
     * tag.<br>
     * The <tt>text</tt> parameter should be the form like:
     * <blockquote><tt>
     * {{ת|אנגלית|firstWord|secondWord|andSoOn
     * </tt></blockquote>
     * Instead of <tt>"אנגלית"</tt> its can be any other language.<br>
     * To the output <tt>span(s)</tt> attached <tt>destinationLanguage</tt>
     * parameter whose value as the language of translation.<br>
     * In the above example its will be <tt>"אנגלית"</tt>.<br>
     * In addition, a <tt>class</tt> attribute with "<tt>translation</tt>" value
     * attached to each output <tt>span</tt>.
     *
     * @param text to process.
     * @return HTML representation of the input.
     */
    protected static String processTranslation(String text) {
        StringBuilder output = new StringBuilder("");
        String[] arr = text.split("\\|");
        if (arr.length == 2) {
            return "";
        }

        String languageOfTranslation = arr[1];
        for (int i = 2; i < arr.length; i++) {
            output.append("<span class=\"translation\" data-destinationLanguage=\"");
            output.append(languageOfTranslation);
            output.append("\">");
            // output.append(arr[i].replaceAll("\\{\\{ת", ""));
            output.append(arr[i]);
            output.append("</span> | ");
        }
        return output.substring(0, output.length() - 3);
    }
}
