package dbFileBuilder.article.nituachDikduki.finders;

import dbFileBuilder.utilities.ArrayUtils;
import dbFileBuilder.utilities.StringUtility;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to extract root(שורש) and gizra(גיזרה) from
 * {@link dbFileBuilder.article.nituachDikduki.NituachDikduki} entry.
 */
public final class RootFinder {

    private static Matcher matcher;
    private static Pattern pattern;
    /**
     * Hold the gizra(גיזרה)
     *
     * @see RootFinder#getGizra()
     */
    private String gizra = "";
    /**
     * Hold the root(שורש)
     *
     * @see RootFinder#getRoot()
     */
    private String rootWord = "";

    /**
     * Attempt to find root. If none found the {@link #rootWord rootWord} is
     * empty string.
     *
     * @param line of text to process.
     */
    public RootFinder(final String line) {
        String str = line;

        if (str.contains("שורש וגזרה=")) {
            findGizra(line);
        }

        str = str.replaceAll("שורש וגזרה=", "");
        str = str.replaceAll("שורש=", "");
        str = str.replaceAll("  ", " ");

        String[] arr = str.split(",|או");

        for (String s : arr) {
            s = s.replaceAll("<small>[^<]*</small>", "");
            if (s.contains("3"))// Three letters root 
            {
                String[] rootArr = s.split("\\||-");
                rootArr // remove  the  {{שרש3
                        = ArrayUtils.removeEltement(rootArr, 0);
                if (rootArr.length >= 4)// too long
                {
                    rootArr = ArrayUtils.removeEltement(rootArr, rootArr.length - 1);
                }
                String thirdRootPhrase = ArrayUtils.join(rootArr, "-");
                thirdRootPhrase = thirdRootPhrase.replaceAll("\\{|\\}", "");

                thirdRootPhrase =// remove unnecessary data
                        thirdRootPhrase.replaceAll("[\\ ;][^\\(\\)]*(?![^[(]+|\\(([^)]*)\\)|\\[[^]]*])", " ");

                rootWord += " " + thirdRootPhrase;

            } else if (s.contains("4")) // Four letters root 
            {
                String fourthRootPhrase = s.replaceAll("\\{\\{שרש4\\|", "");
                fourthRootPhrase = fourthRootPhrase.replaceAll("\\}\\}", "");
                fourthRootPhrase = fourthRootPhrase.replaceAll("\\|", "-");

                rootWord += " " + fourthRootPhrase;
            } else {
                String root = s;
                String additionalInfo = "";
                pattern = // Trying to retrieve additional information
                        Pattern.compile("\\([^\\(]*\\)", Pattern.UNICODE_CASE);
                matcher = pattern.matcher(root);
                if (matcher.find()) {
                    additionalInfo = matcher.group();
                }

                String[] rootArr = s.split("\\|");
                for (String r : rootArr) {
                    if (r.contains("-") && (r.contains("}}") || r.contains("]]"))) {
                        int endIndex = r.indexOf("}}");
                        endIndex = (endIndex == -1) ? r.indexOf("]]") : endIndex;
                        root = r.substring(0, endIndex);
                        root = root.replaceAll("\\[|\\]", "");
                        root += additionalInfo;

                        rootWord += " " + root;
                        break;
                    }
                }
            }
        }

        rootWord = rootWord.replaceAll("[\\ ][^\\ \\)\\(]{1}$", "");
        rootWord = rootWord.replaceAll("\\\\ \\\\ ", " ");
        rootWord = StringUtility.removeNewLine(rootWord);

        rootWord = rootWord.trim();
    }

    /**
     * Attempt to find gizra (verb type). If none found the {@link #gizra gizra}
     * is empty string.
     *
     * @param line of text.
     * @return Nothing, but its assign to <tt>gizra</tt>.
     * @see #gizra
     */
    private void findGizra(String line) {
        pattern
                = Pattern.compile("(גזרת[^\\[\\|]+(\\{\\[|\\|))", Pattern.UNICODE_CASE);
        matcher = pattern.matcher(line);

        if (matcher.find()) { // First attempt 
            gizra = matcher.group();
            gizra = gizra.replaceAll("\\|", "");
            gizra = gizra.replaceAll("\\[\\[|\\]\\]", "");
            gizra = gizra.trim();
            return;
        } else { // Second attempt 
            pattern = // match waht inside the [[ ]]
                    Pattern.compile("\\[\\[[^\\]]*\\]\\]", Pattern.UNICODE_CASE);
            matcher = pattern.matcher(line);
            if (matcher.find()) {
                gizra = matcher.group();
                gizra // Replace all the [[ and all the ]]
                        = gizra.replaceAll("\\[\\[|\\]\\]", "");
                if (!gizra.contains("|")) {
                    gizra = gizra.trim();
                    return;
                }
            }
        }

        // Trying to guss
        if (line.contains("השלמים")) {
            gizra = "גזרת השלמים";
        } else if (line.contains("כפולים")) {
            gizra = "כפולים";
        } else if (line.contains("מרובעים")) {
            gizra = "מרובעים";
        } else if (line.contains("מחומשים")) {
            gizra = "מחומשים";
        } else if (line.contains("משושים")) {
            gizra = "משושים";
        } else if (line.contains("חסרי פ\"נ")) {
            gizra = "חסרי פ\"נ";
        } else if (line.contains("נחי ע\"ו")) {
            gizra = "נחי ע\"ו";
        } else if (line.contains("חפ\"נ")) {
            gizra = "חפ\"נ";
        }
    }

    /**
     * @return gizra(גיזרה). If none exist an empty string is returned.
     * @see RootFinder#findGizra(java.lang.String)
     */
    public String getGizra() {
        return this.gizra;
    }

    /**
     * @return root(שורש). If none exist an empty string is returned.
     */
    public String getRoot() {
        return this.rootWord;
    }
}
