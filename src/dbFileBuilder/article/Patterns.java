package dbFileBuilder.article;

import java.util.regex.Pattern;

/**
 * Storage for useful regular expression
 * {@link java.util.regex.Pattern Patterns}.
 */
public final class Patterns {

    /**
     * Don't let anyone instantiate this class.
     */
    private Patterns() {
    }
    /**
     * Pattern to detect stub markup.
     *
     * @see
     * <a
     * href="http://he.wikipedia.org/wiki/%D7%95%D7%99%D7%A7%D7%99%D7%A4%D7%93%D7%99%D7%94:%D7%A7%D7%A6%D7%A8%D7%9E%D7%A8">ויקיפדיה:קצרמר</a><br>
     */
    protected static final Pattern STUB = Pattern.compile("\\{\\{קצרמר\\}\\}", Pattern.UNICODE_CASE);
    /**
     * @see
     * <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%9C%D7%94%D7%A9%D7%9C%D7%99%D7%9D">תבנית:להשלים</a>
     */
    protected static final Pattern IS_COMPLETED = Pattern.compile("\\{\\{להשלים\\|*.*?\\}\\}", Pattern.UNICODE_CASE);
    /**
     * @see <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A7%D7%98%D7%92%D7%95%D7%A8%D7%99%D7%94">תבנית:קטגוריה</a>
     */
    protected static final Pattern CATEGORIES = Pattern.compile("\\[\\[קטגוריה:.*?\\]\\]", Pattern.UNICODE_CASE);
    /**
     * Pattern to detect root word markup.
     *
     * @see <a
     * href="http://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A9%D7%A8%D7%A9">
     * תבנית:שרש</a>
     */
    protected static final Pattern ROOT = Pattern.compile("\\{\\{שורש\\|.*?\\}\\}", Pattern.UNICODE_CASE);
    /**
     * @see
     * <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%9C%D7%A9%D7%9B%D7%AA%D7%95%D7%91">תבנית:לשכתוב</a>
     */
    protected static final Pattern REWRITING = Pattern.compile("\\{\\{לשכתוב\\}\\}", Pattern.UNICODE_CASE);
    /**
     * Pattern to detect Section headings, e.i : <tt>==headings==</tt><br>
     *
     * Some wikipedians write non standards headings to translation(תרגום)
     * section.<br>
     * Therefor (and anyhow) we don't want digits in headings, or as page title.
     * The purpose of <tt>\\D</tt> is to achieve the mentioned above.
     *
     * @see
     * <a
     * href="http://meta.wikimedia.org/wiki/Help:Editing#Section_headings">Editing
     * MediaWiki - Section headings</a>
     */
    protected static final Pattern SECTION_HEADING = Pattern.compile("^={2}([^=]){1,}={2}$", Pattern.UNICODE_CASE + Pattern.MULTILINE);
    /**
     * Pattern to detect title of pages in German.
     */
    protected static final Pattern GERMAN = Pattern.compile("==( *)גרמנית:.*==", Pattern.UNICODE_CASE);
    /**
     * Pattern to detect title of pages in German.
     */
    protected static final Pattern GERMANIT = Pattern.compile("==גרמנית==", Pattern.UNICODE_CASE);
    /**
     * Pattern to detect title of pages in Icelandic.
     */
    protected static final Pattern ICELANDIC = Pattern.compile("==איסלנדית==", Pattern.UNICODE_CASE);
    /**
     * Pattern to detect curly brackets image syntax.
     */
    protected static final Pattern IMAGE_CURLY = Pattern.compile("\\{\\{[^\\}]+(\\.png|\\.jpg|\\.bmp|\\.svg|\\.gif){1}[^\\}]+\\}\\}", Pattern.UNICODE_CASE + Pattern.CASE_INSENSITIVE);
    /**
     * Pattern to detect square brackets image syntax.
     */
    protected static final Pattern IMAGE = Pattern.compile("\\[\\[[^\\]]+(\\.png|\\.jpg|\\.bmp|\\.svg){1}[^\\]]+\\]\\]", Pattern.UNICODE_CASE + Pattern.CASE_INSENSITIVE);
    /**
     * Pattern to detect square brackets image syntax.<br><br>
     * <b>NOTE</b>:<br>
     * Can be combined with {@link Patterns#IMAGE IMAGE}, but keep it simple.
     */
    protected static final Pattern IMAGE_OTHER_VARIANT = Pattern.compile("\\[\\[[^\\]]+(\\.png|\\.jpg|\\.bmp|\\.svg|\\.gif){1}\\]\\]", Pattern.UNICODE_CASE + Pattern.CASE_INSENSITIVE);
    /**
     * @see <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%90%D7%99%D7%95%D7%AA_%D7%A9%D7%92%D7%95%D7%99">תבנית:איות
     * שגוי</a>
     */
    protected static final Pattern WRONG_SPELLING = Pattern.compile("\\{\\{איות שגוי\\|*.*?\\}\\}", Pattern.UNICODE_CASE);
    /**
     * @see <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%A4%D7%99%D7%A8%D7%95%D7%A9_%D7%A0%D7%95%D7%A1%D7%A3">תבנית:פירוש
     * נוסף</a>
     */
    protected static final Pattern ANOTHER_MEANING = Pattern.compile("(?:\\{\\{(?:פירוש נוסף\\|*):*([^\\}\\|]+)(?:\\|(([^\\{\\}]|(\\{\\{[^\\{\\}]+\\}\\}))*))?\\}\\})", Pattern.MULTILINE + Pattern.UNICODE_CASE);
    /**
     * @see <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%AA%D7%99%D7%A7%D7%95%D7%9F_%D7%9E%D7%99%D7%9C%D7%99%D7%9D_%D7%9E%D7%90%D7%A8%D7%9E%D7%99%D7%AA">תבנית:תיקון
     * מילים מארמית</a>
     */
    protected static final Pattern ARAMAIC_FIX = Pattern.compile("\\{\\{תיקון מילים מארמית\\|*.*?\\}\\}", Pattern.UNICODE_CASE);
    /**
     * @see <a
     * href="https://he.wiktionary.org/wiki/%D7%AA%D7%91%D7%A0%D7%99%D7%AA:%D7%AA%D7%99%D7%A7%D7%95%D7%9F_%D7%9E%D7%99%D7%9C%D7%99%D7%9D_%D7%9E%D7%99%D7%95%D7%95%D7%A0%D7%99%D7%AA">תבנית:תיקון
     * מילים מיוונית</a>
     */
    protected static final Pattern GREEK_FIX = Pattern.compile("\\{\\{תיקון מילים מיוונית\\|*.*?\\}\\}", Pattern.UNICODE_CASE);
    /**
     * Pattern to detect wrong 'see also'(ראו גם) section heading.
     */
    protected static final Pattern SEE_ALSO_MISTAKE = Pattern.compile("==[ ]*ראו גם[ ]*==$", Pattern.MULTILINE + Pattern.UNICODE_CASE);
    /**
     * Pattern to detect wrong synonym(מילים נרדפות) section heading
     */
    protected static final Pattern SYNONYM_MISTAKE = Pattern.compile("==[\\ ]*מילים נרדפות[\\ ]*==$", Pattern.MULTILINE + Pattern.UNICODE_CASE);
}
