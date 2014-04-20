package dbFileBuilder.article.nituachDikduki.finders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to extract root(בניין) from
 * {@link  dbFileBuilder.article.nituachDikduki.NituachDikduki} entry.
 */
public final class BuildingFinder {

    /**
     * הִתְפַּעֵל
     */
    private final static String HITPAEL = "הִתְפַּעֵל";
    /**
     * הִפְעִיל
     */
    private final static String HEFEEL = "הִפְעִיל";
    /**
     * נִפְעַל
     */
    private final static String NIFAAL = "נִפְעַל";
    /**
     * הֻפְעַל
     */
    private final static String HUFAL = "הֻפְעַל";
    /**
     * פִּעֵל
     */
    private final static String PIEL = "פִּעֵל";
    /**
     * פֻּעַל {@value }
     */
    private final static String PUAL = "פֻּעַל";
    private static Matcher matcher;
    private static Pattern pattern;
    /**
     * Current value to process.
     */
    private static String currentValue;

    private BuildingFinder() {// Don't let anyone instantiate this class.
    }

    /**
     * Attempt to find Building value in <tt>line</tt>.<br>
     * To maintain consistency the gender values are clustering and can be one
     * of the followings:
     * <ul>
     * <li>הִתְפַּעֵל</li>
     * <li>הִפְעִיל</li>
     * <li>נִפְעַל</li>
     * <li>הֻפְעַל</li>
     * <li>פִּעֵל</li>
     * <li>פֻּעַל</li>
     * </ul>
     * If none of the mentioned above detected an empty string is returned.
     *
     * @param line of text to process.
     * @return Building value.
     */
    public static String find(String line) {
        currentValue = line;

        if (contains(HITPAEL)) {
            return HITPAEL;
        }
        if (contains(HEFEEL)) {
            return HEFEEL;
        }
        if (contains(NIFAAL)) {
            return NIFAAL;
        }
        if (contains(HUFAL)) {
            return HUFAL;
        }
        if (contains(PIEL)) {
            return PIEL;
        }
        if (contains(PUAL)) {
            return PUAL;
        }

        if (contains("נפעל")) {
            return NIFAAL;
        } else if (contains("קל")) {
            return PUAL;
        } else if (contains("נפעל")) {
            return NIFAAL;
        } else if (contains("פיעל")) {
            return PIEL;
        } else if (contains("התפעל")) {
            return HITPAEL;
        } else if (contains("הפעיל")) {
            return HEFEEL;
        } else if (contains("הופעל")) {
            return HUFAL;
        } else if (contains("פֻּעַל")) {
            return PUAL;
        } else if (contains("פָּעַל"))// Not sure
        {
            return PUAL;
        } else if (contains("פִּעֵל")) {
            return PIEL;
        } else if (contains("הִתְפַּעֵל")) {
            return HITPAEL;
        } else if (contains("פּעֵל")) {
            return PUAL;
        } else if (contains("פֻעַל")) {
            return PUAL;
        } else if (contains("פֻּעל")) {
            return PUAL;
        } else if (contains("פַּעל")) {
            return PUAL;
        } else if (contains("פִעֵל")) {
            return PIEL;
        } else if (contains("פִּיעַל")) {
            return PIEL;
        } else if (contains("התפועל")) {
            return "התפועל";
        } else if (contains("התפועל")) {
            return "התפועל";
        } else if (contains("התפּעל")) {
            return HITPAEL;
        } else if (contains("פָעַל")) {
            return PUAL;
        } else if (contains("פעל")) {
            return PUAL;
        } else if (contains("פועל")) {
            return PUAL;
        } else if (contains("פָּעֵל")) {
            return PUAL;
        }

        //System.out.println(line.trim());  //Uncomment to find problems
        return "";
    }

    /**
     * Returns true if and only if <tt>{@link  #currentValue currentValue}</tt>
     * contains <tt>toLookFor</tt>.
     *
     * @param text to search for.
     * @return true if <tt>{@link  #currentValue currentValue}</tt> string
     * contains <code>text</code>, false otherwise
     */
    private static boolean contains(String text) {
        pattern = Pattern.compile(text, Pattern.UNICODE_CASE);
        matcher = pattern.matcher(currentValue);
        return matcher.find();
    }
}
