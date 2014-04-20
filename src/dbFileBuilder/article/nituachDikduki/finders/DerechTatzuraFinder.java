package dbFileBuilder.article.nituachDikduki.finders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to extract derech tatzura(דרך תצורה) from
 * {@link  dbFileBuilder.article.nituachDikduki.NituachDikduki} entry.
 */
public final class DerechTatzuraFinder {

    private static Matcher matcher;
    private static Pattern pattern;

    private DerechTatzuraFinder() {//  Don't let anyone instantiate this class.
    }

    /**
     * Attempt to find derech tatzura(דרך תצורה) value in <tt>line</tt>.<br>
     *
     * @param line of text to process.
     * @return derech tatzura value.<br>If none found empty string is returned.
     */
    public static String find(String line) {
        if (line.contains("נטיות")) {
            return "";
        }

        line = line.replace("דרך תצורה=", "");
        line = line.trim();

        pattern = Pattern.compile("\\{\\{משקל\\|[^\\}]*\\}\\}", Pattern.UNICODE_CASE);
        matcher = pattern.matcher(line);
        if (matcher.find()) {
            String weight = matcher.group();
            weight = weight.substring(weight.indexOf("|") + 1, weight.indexOf("}"));
            return weight;
        }

        return normalize(line);

    }

    /**
     * Strip WIKI markups and HTML tags.
     *
     * @param wiki text to process.
     * @return The same as <tt>wiki</tt> but without WIKI markups and HTML tags.
     */
    private static String normalize(String wiki) {

        pattern = Pattern.compile("(\\[\\[[^\\|]*\\|)([^\\]]*)(\\]\\])", Pattern.UNICODE_CASE);
        matcher = pattern.matcher(wiki);
        while (matcher.find()) {
            wiki = wiki.replaceFirst("(\\[\\[[^\\|]*\\|)([^\\]]*)(\\]\\])", matcher.group(2));
        }
        wiki = wiki.replaceAll("<[^>]*>", "");
        wiki = wiki.replaceAll("[\\[\\]\\{\\}]", "");
        wiki = wiki.replaceAll("'''|''", "");

        return wiki.trim();
    }
}
